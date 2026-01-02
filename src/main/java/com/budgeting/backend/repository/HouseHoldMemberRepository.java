package com.budgeting.backend.repository;

import com.budgeting.backend.entity.HouseHoldEntity;
import com.budgeting.backend.entity.HouseHoldMemberEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface HouseHoldMemberRepository extends MongoRepository<HouseHoldMemberEntity, ObjectId> {
    Optional<HouseHoldMemberEntity> findByHouseholdIdAndUserId(ObjectId houseHoldId, ObjectId userId);
}
