package com.budgeting.backend.entity;

import com.budgeting.backend.dto.in.Transaction;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "transactions")
@Data
public class TransactionEntity {

    @Id
    private ObjectId id;

    private ObjectId householdId;

    private ObjectId categoryId;

    private ObjectId subCategoryId;

    private ObjectId createdBy;

    private ObjectId updatedBy;

    private Instant createdAt;

    private Instant updatedAt;

    private BigDecimal amount;

    private String notes;

    private boolean isDeleted = false;

    private ObjectId deletedBy;

    private Instant deletedAt;

    // Constructor for creating a new transaction
    public TransactionEntity(Transaction transaction, User user){
        this.householdId = new ObjectId(transaction.getHouseHoldId());
        this.createdBy = user.getId();
        this.updatedBy = null;
        this.amount = transaction.getAmount();
        this.categoryId = new ObjectId(transaction.getCategoryId());
        this.subCategoryId = new ObjectId(transaction.getSubCategoryId());
        this.notes = transaction.getNotes();
        this.createdAt = Instant.now();
        this.updatedAt = null;
    }

    // Copy constructor for auditing or backup purposes
    public TransactionEntity(TransactionEntity other, User user){
        this.id = other.getId();
        this.householdId = other.getHouseholdId();
        this.categoryId = other.getCategoryId();
        this.subCategoryId = other.getSubCategoryId();
        this.createdBy = other.getCreatedBy();
        this.updatedBy = other.getUpdatedBy();
        this.createdAt = other.getCreatedAt();
        this.updatedAt = other.getUpdatedAt();
        this.amount = other.getAmount();
        this.notes = other.getNotes();
        this.isDeleted = other.isDeleted();
        this.deletedBy = other.getDeletedBy();
        this.deletedAt = other.getDeletedAt();
    }
}
