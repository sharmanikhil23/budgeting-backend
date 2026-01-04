package com.budgeting.backend.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private String status;      // e.g., "success" or "error"
    private String message;     // e.g., "Household created successfully"
    private T data;             // custom object, like HouseHoldDto or List<HouseHoldDto>
    private Object errors;      // optional, for validation or server errors
}
