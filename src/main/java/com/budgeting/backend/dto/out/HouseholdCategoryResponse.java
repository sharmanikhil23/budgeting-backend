package com.budgeting.backend.dto.out;

import com.budgeting.backend.global.enums.DefaultCategories;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class HouseholdCategoryResponse {
    private String categoryId;
    private String name;
    private DefaultCategories.CategoryType type;
    private List<HouseholdSubCategoryResponse> subCategories;
}

