package com.budgeting.backend.repository;

import com.budgeting.backend.entity.TransactionAuditEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionAuditRepository extends MongoRepository<TransactionAuditEntity, ObjectId> {
}
