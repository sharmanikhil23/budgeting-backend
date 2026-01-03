package com.budgeting.backend.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @NotBlank(message = "Please provide the house hold id")
    String houseHoldId;

    @NotBlank(message = "Please select the category")
    String categoryId;

    @NotBlank(message = "Please select the sub category")
    String subCategoryId;

    @NotBlank(message = "Amount cannot be null")
    BigDecimal amount;

    String notes;
}
