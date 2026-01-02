package com.budgeting.backend.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseHold {
    @NotNull(message = "HouseHold name is required")
    private String name;

    @NotNull(message = "Please Select Currency")
    private String currency;
}
