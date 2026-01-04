package com.budgeting.backend.service;

import com.budgeting.backend.entity.HouseHoldMemberEntity;
import com.budgeting.backend.global.enums.HouseHoldRoles;
import com.budgeting.backend.repository.HouseHoldMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class HouseHoldMemberService {
    private final HouseHoldMemberRepository memberRepository;

    @Autowired
    public HouseHoldMemberService(HouseHoldMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void saveOwner(ObjectId householdId, ObjectId userId) {
        memberRepository.save(new HouseHoldMemberEntity(householdId, userId, HouseHoldRoles.OWNER));
    }

    public void saveMember(ObjectId householdId, ObjectId userId) {
        memberRepository.save(new HouseHoldMemberEntity(householdId, userId, HouseHoldRoles.MEMBER));
    }

    public List<HouseHoldMemberEntity> findAllByHouseholdId(String householdId) {
        return findAllByHouseholdId(new ObjectId(householdId));
    }

    public List<HouseHoldMemberEntity> findAllByHouseholdId(ObjectId householdId) {
        return memberRepository.findAllByHouseholdId(householdId);
    }

    public boolean isAlreadyMember(ObjectId householdId, ObjectId userId) {
        return memberRepository.findByHouseholdIdAndUserId(householdId, userId).isPresent();
    }

    public void deleteAllByHouseholdId(ObjectId householdId) {
        memberRepository.deleteAllByHouseholdId(householdId);
    }

    public HouseHoldMemberEntity findByHouseholdIdAndUserId(ObjectId houseHoldId, ObjectId userId) {
        return memberRepository.findByHouseholdIdAndUserId(houseHoldId,userId).orElse(null);
    }
}