package com.budgeting.backend.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class HouseholdSubCategoryResponse {
    private String id;
    private String name;
    private String icon;
}

