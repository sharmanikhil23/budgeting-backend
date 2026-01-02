package com.budgeting.backend.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUp extends SignIn {
    @NotBlank(message = "Please provide name")
    private String name;
}
