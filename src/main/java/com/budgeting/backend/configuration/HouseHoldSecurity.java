package com.budgeting.backend.configuration;

import com.budgeting.backend.entity.HouseHoldMemberEntity;
import com.budgeting.backend.global.enums.HouseHoldRoles;
import com.budgeting.backend.repository.HouseHoldMemberRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("houseHoldSecurity")
public class HouseHoldSecurity {

    @Autowired
    private HouseHoldMemberRepository houseHoldMemberRepository;

    private HouseHoldMemberEntity getHouseHoldMemberEntity(String houseHoldId, String userId){
        return houseHoldMemberRepository.findByHouseholdIdAndUserId(new ObjectId(houseHoldId), new ObjectId(userId)).orElse(null);
    }

    private HouseHoldMemberEntity getHouseHoldMemberEntity(ObjectId houseHoldId, ObjectId userId){
        return houseHoldMemberRepository.findByHouseholdIdAndUserId(houseHoldId, userId).orElse(null);
    }

    public boolean isUserPartOfHouseHold(String houseHoldId, String userId){
        HouseHoldMemberEntity houseHoldMemberEntity = getHouseHoldMemberEntity(houseHoldId,userId);
        if(houseHoldMemberEntity!=null){
            return true;
        }else{
            return false;
        }
    }

    //Checks if the user is part of household as well as if user is admin or owner
    public boolean isAdminOrOwner(String houseHoldId, String userId) {
        HouseHoldMemberEntity houseHoldMemberEntity = getHouseHoldMemberEntity(houseHoldId,userId);

       if(houseHoldMemberEntity != null && (houseHoldMemberEntity.getRole() == HouseHoldRoles.ADMIN || houseHoldMemberEntity.getRole() == HouseHoldRoles.OWNER )){
           return true;
       }else{
           return false;
        }
    }
}

