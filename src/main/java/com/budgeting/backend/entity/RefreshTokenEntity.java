package com.budgeting.backend.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "refreshToken")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class RefreshTokenEntity {
    @Id
    private String id;

    @NotNull
    private ObjectId userID;

    @NotNull
    String refreshToken;

    public RefreshTokenEntity(ObjectId userID, String refreshToken){
        this.userID = userID;
        this.refreshToken = refreshToken;
    }
}
