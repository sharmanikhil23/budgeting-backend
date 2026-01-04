package com.budgeting.backend.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Document(collection = "transaction_audit")
@Data
public class TransactionAuditEntity {

    @Id
    private ObjectId id;

    private ObjectId transactionId; // reference to the main transaction

    private ObjectId householdId;

    private ObjectId performedBy; // who performed the action

    private Instant performedAt;

    private String action; // CREATE, UPDATE, DELETE

    private BigDecimal oldAmount;
    private BigDecimal newAmount;

    private ObjectId oldCategoryId;
    private ObjectId newCategoryId;

    private ObjectId oldSubCategoryId;
    private ObjectId newSubCategoryId;

    private String oldNotes;
    private String newNotes;

    private Boolean oldIsDeleted;
    private Boolean newIsDeleted;

    public TransactionAuditEntity(TransactionEntity oldTransaction, TransactionEntity newTransaction, ObjectId performedBy, String action){
        this.transactionId = oldTransaction != null ? oldTransaction.getId() : (newTransaction != null ? newTransaction.getId() : null);
        this.householdId = oldTransaction != null ? oldTransaction.getHouseholdId() : newTransaction.getHouseholdId();
        this.performedBy = performedBy;
        this.performedAt = Instant.now();
        this.action = action;

        if(oldTransaction != null){
            this.oldAmount = oldTransaction.getAmount();
            this.oldCategoryId = oldTransaction.getCategoryId();
            this.oldSubCategoryId = oldTransaction.getSubCategoryId();
            this.oldNotes = oldTransaction.getNotes();
            this.oldIsDeleted = oldTransaction.isDeleted();
        }

        if(newTransaction != null){
            this.newAmount = newTransaction.getAmount();
            this.newCategoryId = newTransaction.getCategoryId();
            this.newSubCategoryId = newTransaction.getSubCategoryId();
            this.newNotes = newTransaction.getNotes();
            this.newIsDeleted = newTransaction.isDeleted();
        }
    }
}
