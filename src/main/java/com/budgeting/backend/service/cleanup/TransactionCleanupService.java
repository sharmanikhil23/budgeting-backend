package com.budgeting.backend.service.cleanup;

import com.budgeting.backend.entity.TransactionAuditEntity;
import com.budgeting.backend.entity.TransactionEntity;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class TransactionCleanupService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Transactional
    public void hardDeleteUserData(ObjectId userId, ObjectId householdId) {
        // 1. Identify ALL transactions created by this user in this specific household
        Query transQuery = new Query(Criteria.where("householdId").is(householdId)
                .and("createdBy").is(userId));

        // Find the IDs of the transactions we are about to kill
        List<ObjectId> transIds = mongoTemplate.find(transQuery, TransactionEntity.class)
                .stream()
                .map(TransactionEntity::getId)
                .toList();

        if (!transIds.isEmpty()) {
            // 2. CRITICAL FIX: Delete ALL audits linked to these transactions
            // It doesn't matter who performed the audit (User A, B, or C)
            // If the transaction is gone, the history must go too to prevent orphans.
            Query auditDeleteQuery = new Query(Criteria.where("transactionId").in(transIds).and("householdId").in(householdId));
            long deletedAudits = mongoTemplate.remove(auditDeleteQuery, TransactionAuditEntity.class).getDeletedCount();

            // 3. Delete the transactions themselves
            long deletedTransactions = mongoTemplate.remove(transQuery, TransactionEntity.class).getDeletedCount();

            log.info("Purged {} transactions and {} associated audit records for user {} in household {}",
                    deletedTransactions, deletedAudits, userId, householdId);
        } else {
            log.info("No transactions found for user {} in household {}", userId, householdId);
        }
    }
}
