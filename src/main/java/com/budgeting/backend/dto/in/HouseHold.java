package com.budgeting.backend.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseHold {
    @NotNull(message = "HouseHold name is required")
    private String name;

    @NotNull(message = "Must provide the time in utc when you tried to do something")
    private Instant time;

    private String currency;
}
