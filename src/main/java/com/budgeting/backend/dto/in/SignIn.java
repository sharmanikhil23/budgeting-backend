package com.budgeting.backend.dto.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
public class SignIn{
    @Email(message = "Please use Valid Email")
    @NotBlank(message = "Please provide email")
    private String email;

    @NotBlank(message = "Please provide password")
    private String password;

    @JsonCreator
    public SignIn(
            @JsonProperty("email") String email,
            @JsonProperty("password") String password
    ) {
        this.email = email.toLowerCase();
        this.password = password;
    }
}
