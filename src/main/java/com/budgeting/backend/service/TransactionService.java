package com.budgeting.backend.service;

import com.budgeting.backend.configuration.HouseHoldSecurity;
import com.budgeting.backend.configuration.TransactionSecurity;
import com.budgeting.backend.dto.in.Transaction;
import com.budgeting.backend.dto.in.UpdateTransaction;
import com.budgeting.backend.entity.TransactionAuditEntity;
import com.budgeting.backend.entity.TransactionEntity;
import com.budgeting.backend.entity.User;
import com.budgeting.backend.repository.TransactionAuditRepository;
import com.budgeting.backend.repository.TransactionRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionAuditRepository transactionAuditRepository;
    private final TransactionSecurity transactionSecurity;
    private final HouseHoldSecurity houseHoldSecurity;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              TransactionAuditRepository transactionAuditRepository,
                              TransactionSecurity transactionSecurity,
                              HouseHoldSecurity houseHoldSecurity){
        this.transactionRepository = transactionRepository;
        this.transactionAuditRepository = transactionAuditRepository;
        this.transactionSecurity = transactionSecurity;
        this.houseHoldSecurity = houseHoldSecurity;
    }

    @PreAuthorize("@houseHoldSecurity.isUserPartOfHouseHold(#transaction.houseHoldId, #user.id)")
    public ResponseEntity<HashMap<String,Object>> save(Transaction transaction, User user){
        TransactionEntity transactionEntity = transactionRepository.save(new TransactionEntity(transaction,user));

        // Create audit record
        TransactionAuditEntity audit = new TransactionAuditEntity(
                null,
                transactionEntity,
                user.getId(),
                "CREATE"
        );
        transactionAuditRepository.save(audit);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HashMap<>() {{
                    put("status", "success");
                    put("message", "Created Successfully");
                    put("error", null);
                }}
        );
    }

    @PreAuthorize(
            "@houseHoldSecurity.isUserPartOfHouseHold(#transaction.houseHoldId, #user.id) && " +
                    "@transactionSecurity.isCreatedByUserOrAdminOrAbove(#transaction.houseHoldId, #user.id)"
    )
    public ResponseEntity<?> update(UpdateTransaction transaction, User user) {
        TransactionEntity transactionEntity = transactionRepository.findById(new ObjectId(transaction.getTransactionId())).orElse(null);

        if(transactionEntity != null){
            // Keep old copy for audit
            TransactionEntity oldTransaction = new TransactionEntity(transactionEntity, user);

            transactionEntity.setUpdatedBy(user.getId());
            transactionEntity.setUpdatedAt(Instant.now());
            transactionEntity.setAmount(transaction.getAmount());
            transactionEntity.setNotes(transaction.getNotes());
            transactionEntity.setCategoryId(new ObjectId(transaction.getCategoryId()));
            transactionEntity.setSubCategoryId(new ObjectId(transaction.getSubCategoryId()));

            transactionRepository.save(transactionEntity);

            // Save audit
            TransactionAuditEntity audit = new TransactionAuditEntity(
                    oldTransaction,
                    transactionEntity,
                    user.getId(),
                    "UPDATE"
            );
            transactionAuditRepository.save(audit);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new HashMap<>() {{
                        put("status", "success");
                        put("message", "Updated Successfully");
                        put("error", null);
                    }}
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new HashMap<>() {{
                    put("status", "error");
                    put("message", "Transaction not found");
                    put("error", "NOT_FOUND");
                }}
        );
    }

    @PreAuthorize(
            "@houseHoldSecurity.isUserPartOfHouseHold(#houseHoldId, #user.id) && " +
                    "@transactionSecurity.isCreatedByUserOrAdminOrAbove(#houseHoldId, #user.id)"
    )
    public ResponseEntity<?> deleteTransaction(String houseHoldId, String transactionId, User user){
        TransactionEntity entity = transactionRepository.findById(new ObjectId(transactionId)).orElse(null);

        if(entity != null){
            TransactionEntity oldTransaction = new TransactionEntity(entity, user);

            entity.setDeleted(true);
            entity.setDeletedBy(user.getId());
            entity.setDeletedAt(Instant.now());
            transactionRepository.save(entity);

            TransactionAuditEntity audit = new TransactionAuditEntity(oldTransaction, entity, user.getId(), "DELETE");
            transactionAuditRepository.save(audit);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new HashMap<>() {{
                        put("status", "success");
                        put("message", "Deleted Successfully. User has 7 days to undo their changes");
                        put("error", null);
                    }}
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new HashMap<>() {{
                    put("status", "error");
                    put("message", "Transaction not found");
                    put("error", "NOT_FOUND");
                }}
        );
    }

    @PreAuthorize(
            "@houseHoldSecurity.isUserPartOfHouseHold(#houseHoldId, #user.id) && " +
                    "@transactionSecurity.isCreatedByUserOrAdminOrAbove(#houseHoldId, #user.id)"
    )
    public ResponseEntity<?> undoTransaction(String houseHoldId, String transactionId, User user){
        TransactionEntity entity = transactionRepository.findById(new ObjectId(transactionId)).orElse(null);

        if(entity != null){
            TransactionEntity oldTransaction = new TransactionEntity(entity, user);

            entity.setDeleted(false);
            entity.setUpdatedAt(Instant.now());
            entity.setUpdatedBy(user.getId());
            entity.setDeletedBy(null);
            entity.setDeletedAt(null);
            transactionRepository.save(entity);

            TransactionAuditEntity audit = new TransactionAuditEntity(oldTransaction, entity, user.getId(), "UNDO_DELETE");
            transactionAuditRepository.save(audit);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new HashMap<>() {{
                        put("status", "success");
                        put("message", "Undo Successful");
                        put("error", null);
                    }}
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new HashMap<>() {{
                    put("status", "error");
                    put("message", "Transaction not found");
                    put("error", "NOT_FOUND");
                }}
        );
    }
}
