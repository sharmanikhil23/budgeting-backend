package com.budgeting.backend.repository;

import com.budgeting.backend.entity.HouseHoldEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HouseHoldRepository extends MongoRepository<HouseHoldEntity, ObjectId> {
}
