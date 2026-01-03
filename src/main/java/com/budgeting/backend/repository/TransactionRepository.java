package com.budgeting.backend.repository;

import com.budgeting.backend.entity.TransactionEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<TransactionEntity, ObjectId> {
}
