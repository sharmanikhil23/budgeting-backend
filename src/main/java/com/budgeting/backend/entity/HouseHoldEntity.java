package com.budgeting.backend.entity;

import com.budgeting.backend.dto.in.HouseHold;
import com.budgeting.backend.global.enums.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Document("households")
@Getter
@NoArgsConstructor
public class HouseHoldEntity {

    @MongoId
    private ObjectId id;

    @NotNull
    private String name;

    @NotNull
    @Indexed(unique = true)
    private ObjectId createdBy; // user who created the household

    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    private Currency currency;

    public HouseHoldEntity(HouseHold houseHold, ObjectId createdBy){
        this.name = houseHold.getName();
        this.currency = Currency.valueOf(houseHold.getCurrency());
        createdAt = updatedAt = Instant.now();
        this.createdBy = createdBy;
    }
}
