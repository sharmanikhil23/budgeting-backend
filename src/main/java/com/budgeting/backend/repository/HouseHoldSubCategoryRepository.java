package com.budgeting.backend.repository;

import com.budgeting.backend.entity.HouseHoldSubCategory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HouseHoldSubCategoryRepository extends MongoRepository<HouseHoldSubCategory, ObjectId> {
}
