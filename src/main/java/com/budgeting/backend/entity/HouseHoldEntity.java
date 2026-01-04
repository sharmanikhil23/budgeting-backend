package com.budgeting.backend.entity;

import com.budgeting.backend.dto.in.HouseHold;
import com.budgeting.backend.global.enums.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Document("households")
@Getter
@Setter
@NoArgsConstructor
public class HouseHoldEntity {

    @MongoId
    private ObjectId id = new ObjectId();

    @NotNull
    private String name;

    @Version  // <-- Add this!
    private Long version;

    @NotNull
    @Indexed
    private ObjectId createdBy; // user who created the household

    private Instant createdAt;

    private Instant updatedAt;

    private ObjectId updatedBy;

    @NotNull
    private Currency currency;

    private boolean isDeleted = false;
    private Instant deletedAt;
    private Instant purgeDate;
    private ObjectId deletedBy;

    public HouseHoldEntity(HouseHold houseHold, ObjectId createdBy){
        this.name = houseHold.getName();
        this.currency = Currency.valueOf(houseHold.getCurrency().toUpperCase());
        this.createdAt = houseHold.getTime() != null ? houseHold.getTime() : Instant.now();
        this.createdBy = createdBy;
        this.updatedAt = null; // will be set on update
        this.updatedBy = null; // will be set on update
    }
}

