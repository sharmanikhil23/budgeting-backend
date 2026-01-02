package com.budgeting.backend.entity;

import com.budgeting.backend.global.enums.HouseHoldRoles;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Document("household_member")
@Getter
@NoArgsConstructor
public class HouseHoldMemberEntity {

    @MongoId
    private ObjectId id;

    @NotNull
    @Indexed(unique = true)
    private ObjectId householdId;

    @NotNull
    @Indexed(unique = true)
    private ObjectId userId;

    @NotNull
    private HouseHoldRoles role;

    private Instant joinedAt;

    private Instant leftAt;

    public HouseHoldMemberEntity(ObjectId householdId, ObjectId userId, HouseHoldRoles role) {
        this.householdId =householdId;
        this.userId = userId;
        this.role = role;
        this.joinedAt = Instant.now();
        this.leftAt = null;
    }

}
