package com.budgeting.backend.service;

import com.budgeting.backend.configuration.CachingConfig;
import com.budgeting.backend.configuration.HouseHoldSecurity;
import com.budgeting.backend.dto.in.HouseHold;
import com.budgeting.backend.dto.out.ApiResponseDTO;
import com.budgeting.backend.dto.out.HouseHoldDTO;
import com.budgeting.backend.entity.*;
import com.budgeting.backend.global.PasswordHelper;
import com.budgeting.backend.global.enums.Currency;
import com.budgeting.backend.global.enums.HouseHoldRoles;
import com.budgeting.backend.repository.HouseHoldCategoryRepository;
import com.budgeting.backend.repository.HouseHoldRepository;
import com.budgeting.backend.repository.HouseHoldSubCategoryRepository;
import com.budgeting.backend.service.cleanup.TransactionCleanupService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

import java.time.Instant;
import java.util.HashMap;

@Service
public class HouseHoldService {

    private final HouseHoldRepository houseHoldRepository;
    private final HouseHoldMemberService houseHoldMemberService;
    private final AuthenticationService authenticationService;
    private final HouseHoldSecurity houseHoldSecurity;
    private CacheManager cacheManager;
    private final HouseHoldCategoryRepository categoryRepository;
    private final HouseHoldSubCategoryRepository subCategoryRepository;
    private final TransactionCleanupService transactionCleanupService; // Assuming this is injected

    @Value("${cache.life}") private long life;

    @Autowired
    public HouseHoldService(
            HouseHoldRepository houseHoldRepository,
            HouseHoldMemberService houseHoldMemberService,
            AuthenticationService authenticationService,
            HouseHoldSecurity houseHoldSecurity,
            CacheManager cacheManager,
            HouseHoldCategoryRepository categoryRepository,
            HouseHoldSubCategoryRepository subCategoryRepository,
            TransactionCleanupService transactionCleanupService
    ) {
        this.houseHoldRepository = houseHoldRepository;
        this.houseHoldMemberService = houseHoldMemberService;
        this.authenticationService = authenticationService;
        this.houseHoldSecurity = houseHoldSecurity;
        this.cacheManager = cacheManager;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.transactionCleanupService = transactionCleanupService;
    }

    @Transactional
    public ResponseEntity<ApiResponseDTO<HouseHoldDTO>> makeHouseHold(HouseHold houseHold, User user) {
        HouseHoldEntity entity = new HouseHoldEntity(houseHold, user.getId());
        entity = houseHoldRepository.save(entity);

        houseHoldMemberService.saveOwner(entity.getId(), user.getId());

        HouseHoldDTO dto = new HouseHoldDTO(entity);
        ApiResponseDTO<HouseHoldDTO> response = new ApiResponseDTO<>("success", "Household created successfully", dto, null);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    @PreAuthorize("@houseHoldSecurity.isAdminOrOwner(#houseHoldId, #userId)")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> getInviteCode(String houseHoldId, User userId) {
        ObjectId householdObjectId = new ObjectId(houseHoldId);

        if (!houseHoldRepository.existsById(householdObjectId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("error", "Household not found", null, "NOT_FOUND"));
        }

        String inviteCode = PasswordHelper.generateInviteCode();
        CachingConfig.InviteInfo inviteInfo = new CachingConfig.InviteInfo(householdObjectId);

        cacheManager.getCache(CachingConfig.INVITE_CODES_CACHE).put(inviteCode, inviteInfo);

        Map<String, String> data = new HashMap<>();
        data.put("inviteCode", inviteCode);
        data.put("expiresInMinutes", life + "ms");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("success", "Invite code generated", data, null));
    }

    @Transactional
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> addUsersInHouseHold(String inviteCode, User userId) {
        var cache = cacheManager.getCache(CachingConfig.INVITE_CODES_CACHE);
        CachingConfig.InviteInfo inviteInfo = cache.get(inviteCode, CachingConfig.InviteInfo.class);

        if (inviteInfo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>("error", "Invalid or expired invite code", null, "INVALID_CODE"));
        }

        cache.evict(inviteCode);

        boolean alreadyMember = houseHoldMemberService.findByHouseholdIdAndUserId(inviteInfo.houseHoldId(), userId.getId()) != null;

        if (alreadyMember) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponseDTO<>("error", "User already part of household", null, "ALREADY_MEMBER"));
        }

        houseHoldMemberService.saveMember(inviteInfo.houseHoldId(), userId.getId());

        Map<String, String> data = new HashMap<>();
        data.put("houseHoldId", inviteInfo.houseHoldId().toString());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("success", "Joined household successfully", data, null));
    }

    @Transactional
    @PreAuthorize("@houseHoldSecurity.isAdminOrOwner(#houseHoldId, #user.id)")
    public ResponseEntity<ApiResponseDTO<HouseHoldDTO>> updateHouseHold(HouseHold houseHold, String houseHoldId, User user) {
        HouseHoldEntity entity = houseHoldRepository.findById(new ObjectId(houseHoldId))
                .orElseThrow(() -> new IllegalArgumentException("Household not found"));

        entity.setName(houseHold.getName());
        entity.setCurrency(Currency.valueOf(houseHold.getCurrency()));
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy(user.getId());

        entity = houseHoldRepository.save(entity);

        HouseHoldDTO dto = new HouseHoldDTO(entity);
        return ResponseEntity.ok(new ApiResponseDTO<>("success", "Updated successfully", dto, null));
    }

    @Transactional
    @PreAuthorize("@houseHoldSecurity.isOwner(#houseHoldId, #user.id)")
    public ResponseEntity<ApiResponseDTO<Object>> deleteHouseHold(String houseHoldId, User user, Boolean deleteNow) {
        ObjectId householdObjectId = new ObjectId(houseHoldId);
        HouseHoldEntity entity = houseHoldRepository.findById(householdObjectId)
                .orElseThrow(() -> new IllegalArgumentException("Household not found"));

        if (deleteNow) {
            performFullSystemPurge(houseHoldId);
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Household and all associated data purged permanently", null, null));
        } else {
            entity.setDeleted(true);
            entity.setDeletedBy(user.getId());
            entity.setDeletedAt(Instant.now());
            // Ensure HouseHoldEntity has purgeDate field
            entity.setPurgeDate(Instant.now().plus(7, java.time.temporal.ChronoUnit.DAYS));

            houseHoldRepository.save(entity);
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Household scheduled for deletion. You have 7 days to recover it.", null, null));
        }
    }

    private void performFullSystemPurge(String houseHoldId) {
        ObjectId householdObjectId = new ObjectId(houseHoldId);

        List<HouseHoldMemberEntity> houseHoldMemberEntityList =
                houseHoldMemberService.findAllByHouseholdId(houseHoldId);

        // deleting all the transaction and its auditing from one userAtTime
        for(int i=0;i<houseHoldMemberEntityList.size();i++){
            transactionCleanupService.hardDeleteUserData(houseHoldMemberEntityList.get(i).getUserId(), householdObjectId);
        }

        // Now deleting all the householder members from "household members" entity
        houseHoldMemberService.deleteAllByHouseholdId(householdObjectId);

        // todo now will be deleting all of the HouseholdCategoryMonthly as well as other left over data like
        // HouseHoldCategory, SubCategory

    }
}
