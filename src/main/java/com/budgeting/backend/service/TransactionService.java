package com.budgeting.backend.service;

import com.budgeting.backend.configuration.HouseHoldSecurity;
import com.budgeting.backend.configuration.TransactionSecurity;
import com.budgeting.backend.dto.in.Transaction;
import com.budgeting.backend.dto.in.UpdateTransaction;
import com.budgeting.backend.dto.out.ApiResponseDTO;
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
    public ResponseEntity<ApiResponseDTO<Object>> save(Transaction transaction, User user) {
        TransactionEntity transactionEntity = transactionRepository.save(new TransactionEntity(transaction, user));
        transactionAuditRepository.save(new TransactionAuditEntity(null, transactionEntity, user.getId(), "CREATE"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("success", "Created Successfully", null, null));
    }

    @PreAuthorize("@houseHoldSecurity.isUserPartOfHouseHold(#transaction.houseHoldId, #user.id) && @transactionSecurity.isCreatedByUserOrAdminOrAbove(#transaction.houseHoldId, #user.id)")
    public ResponseEntity<ApiResponseDTO<Object>> update(UpdateTransaction transaction, User user) {
        TransactionEntity entity = transactionRepository.findById(new ObjectId(transaction.getTransactionId()))
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        TransactionEntity oldTransaction = new TransactionEntity(entity, user);

        // Update fields...
        entity.setAmount(transaction.getAmount());
        entity.setNotes(transaction.getNotes());
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy(user.getId());

        transactionRepository.save(entity);
        transactionAuditRepository.save(new TransactionAuditEntity(oldTransaction, entity, user.getId(), "UPDATE"));

        return ResponseEntity.ok(new ApiResponseDTO<>("success", "Updated Successfully", null, null));
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
