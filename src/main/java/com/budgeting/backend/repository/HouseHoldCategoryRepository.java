package com.budgeting.backend.repository;

import com.budgeting.backend.entity.HouseHoldCategory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HouseHoldCategoryRepository extends MongoRepository<HouseHoldCategory, ObjectId> {
}
