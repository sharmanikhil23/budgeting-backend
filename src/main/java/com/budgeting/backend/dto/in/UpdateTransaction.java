package com.budgeting.backend.dto.in;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransaction extends Transaction{
    @NotNull(message = "Please provide the transaction id")
    String transactionId;
}
