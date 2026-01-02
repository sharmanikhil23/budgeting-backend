package com.budgeting.backend.entity;

import com.budgeting.backend.dto.in.SignUp;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.Collection;
import java.util.Locale;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {

    @MongoId
    private ObjectId id;

    private String password;

    @Indexed(unique = true)
    private String email;

    private String name;

    private boolean enabled;

    private Instant createdAt;

    private Instant updatedAt;

    public User(SignUp signUp){
        this.name = signUp.getName();
        this.email = signUp.getEmail().trim().toLowerCase();
        this.password = signUp.getPassword();
        this.createdAt = this.updatedAt = Instant.now();
        this.enabled = true;
    }
}
