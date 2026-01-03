package com.budgeting.backend.entity;

import com.budgeting.backend.global.enums.HouseHoldRoles;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Document("household_member")
@Data
@Getter
@NoArgsConstructor
@CompoundIndex(
        name = "household_user_unique",
        def = "{'householdId': 1, 'userId': 1}",
        unique = true
)
public class HouseHoldMemberEntity {

    @Id
    private ObjectId id;

    @NotNull
    @Indexed
    private ObjectId householdId;

    @NotNull
    @Indexed
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
