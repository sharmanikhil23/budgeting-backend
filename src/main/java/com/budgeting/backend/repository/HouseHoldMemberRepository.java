package com.budgeting.backend.repository;

import com.budgeting.backend.entity.HouseHoldEntity;
import com.budgeting.backend.entity.HouseHoldMemberEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface HouseHoldMemberRepository extends MongoRepository<HouseHoldMemberEntity, ObjectId> {
    Optional<HouseHoldMemberEntity> findByHouseholdIdAndUserId(ObjectId houseHoldId, ObjectId userId);

    List<HouseHoldMemberEntity> findAllByHouseholdId(ObjectId houseHoldId);

    void deleteAllByHouseholdId(ObjectId houseHoldId);
}
