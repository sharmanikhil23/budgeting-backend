package com.budgeting.backend.service;

import com.budgeting.backend.configuration.CachingConfig;
import com.budgeting.backend.configuration.HouseHoldSecurity;
import com.budgeting.backend.dto.in.HouseHold;
import com.budgeting.backend.entity.*;
import com.budgeting.backend.global.PasswordHelper;
import com.budgeting.backend.global.enums.Currency;
import com.budgeting.backend.global.enums.HouseHoldRoles;
import com.budgeting.backend.repository.HouseHoldCategoryRepository;
import com.budgeting.backend.repository.HouseHoldMemberRepository;
import com.budgeting.backend.repository.HouseHoldRepository;
import com.budgeting.backend.repository.HouseHoldSubCategoryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;

@Service
public class HouseHoldService {

    private final HouseHoldRepository houseHoldRepository;
    private final HouseHoldMemberRepository houseHoldMemberRepository;
    private final AuthenticationService authenticationService;
    private final HouseHoldSecurity houseHoldSecurity;
    private CacheManager cacheManager;
    private final HouseHoldCategoryRepository categoryRepository;
    private final HouseHoldSubCategoryRepository subCategoryRepository;

    @Value("${cache.life}") private long life;

    @Autowired
    public HouseHoldService(
            HouseHoldRepository houseHoldRepository,
            HouseHoldMemberRepository houseHoldMemberRepository,
            AuthenticationService authenticationService,
            HouseHoldSecurity houseHoldSecurity,
            CacheManager cacheManager,
            HouseHoldCategoryRepository categoryRepository,
            HouseHoldSubCategoryRepository subCategoryRepository
    ) {
        this.houseHoldRepository = houseHoldRepository;
        this.houseHoldMemberRepository = houseHoldMemberRepository;
        this.authenticationService = authenticationService;
        this.houseHoldSecurity = houseHoldSecurity;
        this.cacheManager = cacheManager;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
    }


    @Transactional
    public ResponseEntity<HashMap<String,String>> makeHouseHold(HouseHold houseHold, User user) {

        HouseHoldEntity entity = new HouseHoldEntity(houseHold, user.getId());
        entity = houseHoldRepository.save(entity);

        HouseHoldMemberEntity owner = new HouseHoldMemberEntity(
                entity.getId(),
                user.getId(),
                HouseHoldRoles.OWNER
        );

        houseHoldMemberRepository.save(owner);

        HouseHoldEntity finalEntity = entity;

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HashMap<>() {{
                    put("status", "success");
                    put("message", "Household created successfully");
                    put("houseHoldId", finalEntity.getId().toString());
                }}
        );
    }

    @Transactional
    @PreAuthorize("@houseHoldSecurity.isAdminOrOwner(#houseHoldId, #userId)")
    public ResponseEntity<HashMap<String,String>> getInviteCode(String houseHoldId, User userId) {

        ObjectId householdObjectId = new ObjectId(houseHoldId);

        if (!houseHoldRepository.existsById(householdObjectId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new HashMap<>() {{
                        put("status", "error");
                        put("message", "Household not found");
                    }}
            );
        }

        String inviteCode = PasswordHelper.generateInviteCode();

        CachingConfig.InviteInfo inviteInfo =
                new CachingConfig.InviteInfo(householdObjectId);

        cacheManager
                .getCache(CachingConfig.INVITE_CODES_CACHE)
                .put(inviteCode, inviteInfo);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HashMap<>() {{
                    put("status", "success");
                    put("inviteCode", inviteCode);
                    put("expiresInMinutes",life+ "ms");
                }}
        );
    }

    @Transactional
    public ResponseEntity<?> addUsersInHouseHold(String inviteCode, User userId) {

        var cache = cacheManager.getCache(CachingConfig.INVITE_CODES_CACHE);

        CachingConfig.InviteInfo inviteInfo =
                cache.get(inviteCode, CachingConfig.InviteInfo.class);

        if (inviteInfo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new HashMap<>() {{
                        put("status", "error");
                        put("message", "Invalid or expired invite code");
                    }}
            );
        }

        // prevent reuse
        cache.evict(inviteCode);

        boolean alreadyMember =
                houseHoldMemberRepository
                        .findByHouseholdIdAndUserId(inviteInfo.houseHoldId(), userId.getId()).orElse(null) != null;

        if (alreadyMember) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new HashMap<>() {{
                        put("status", "error");
                        put("message", "User already part of household");
                    }}
            );
        }

        HouseHoldMemberEntity member = new HouseHoldMemberEntity(
                inviteInfo.houseHoldId(),
                userId.getId(),
                HouseHoldRoles.MEMBER
        );

        houseHoldMemberRepository.save(member);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HashMap<>() {{
                    put("status", "success");
                    put("message", "Joined household successfully");
                    put("houseHoldId", inviteInfo.houseHoldId().toString());
                }}
        );
    }

    @Transactional
    @PreAuthorize("@houseHoldSecurity.isAdminOrOwner(#houseHoldId, #user.id)")
    public ResponseEntity<?> updateHouseHold(HouseHold houseHold, String houseHoldId, User user) {

        HouseHoldEntity entity = houseHoldRepository
                .findById(new ObjectId(houseHoldId))
                .orElseThrow(() -> new IllegalArgumentException("Household not found"));

        entity.setUpdatedAt(Instant.now());
        entity.setName(houseHold.getName());
        entity.setCurrency(Currency.valueOf(houseHold.getCurrency()));

        houseHoldRepository.save(entity);

        return ResponseEntity.ok(
                new HashMap<>() {{
                    put("status", "success");
                    put("houseHoldId", entity.getId().toString());
                }}
        );
    }

    @PreAuthorize("@houseHoldSecurity.isAdminOrOwner(#houseHoldId, #user.id)")
    public ResponseEntity<?> deleteHouseHold(String houseHoldId, User user){
        houseHoldRepository.deleteById(new ObjectId(houseHoldId));
        if(houseHoldRepository.findById(new ObjectId(houseHoldId)).orElse(null) == null){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
