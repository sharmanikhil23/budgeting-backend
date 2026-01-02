package com.budgeting.backend.service;

import com.budgeting.backend.configuration.CatchingConfig;
import com.budgeting.backend.configuration.HouseHoldSecurity;
import com.budgeting.backend.dto.in.HouseHold;
import com.budgeting.backend.entity.*;
import com.budgeting.backend.global.PasswordHelper;
import com.budgeting.backend.global.enums.HouseHoldRoles;
import com.budgeting.backend.repository.HouseHoldCategoryRepository;
import com.budgeting.backend.repository.HouseHoldMemberRepository;
import com.budgeting.backend.repository.HouseHoldRepository;
import com.budgeting.backend.repository.HouseHoldSubCategoryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseEntity<?> getInviteCode(String houseHoldId, ObjectId userId){
        String code = PasswordHelper.generateSixDigitCode();

        CatchingConfig.InviteInfo inviteInfo = new CatchingConfig.InviteInfo(new ObjectId(houseHoldId), code);

        cacheManager.getCache("inviteCodes").put(code, inviteInfo);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HashMap<>() {{
                    put("status", "success");
                    put("message", new HashMap<>(){{
                        put("inviteCode",code);
                    }});
                }}
        );
    }

    @Transactional
    public ResponseEntity<?> addUsersInHouseHold(String inviteCode, ObjectId id) {
        CatchingConfig.InviteInfo inviteInfo = cacheManager.getCache("inviteCodes").get(inviteCode,
                CatchingConfig.InviteInfo.class);

        HouseHoldMemberEntity owner = new HouseHoldMemberEntity(
                inviteInfo.houseHoldId(),
                id,
                HouseHoldRoles.MEMBER
        );

        houseHoldMemberRepository.save(owner);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HashMap<>() {{
                    put("status", "success");
                    put("message", "Added Successfully");
                    put("houseHoldId", inviteInfo.houseHoldId());
                }}
        );

    }
}
