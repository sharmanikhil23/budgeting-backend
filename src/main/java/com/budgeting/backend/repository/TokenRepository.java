package com.budgeting.backend.repository;

import com.budgeting.backend.entity.RefreshTokenEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<RefreshTokenEntity, ObjectId> {
}
