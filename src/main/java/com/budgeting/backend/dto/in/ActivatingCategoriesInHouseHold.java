package com.budgeting.backend.dto.in;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivatingCategoriesInHouseHold {
    @NotNull(message = "Category Id must be provided")
    String categoryId;

    @NotNull(message = "must be provided if user want to activate it or not")
    boolean isActive;

    @NotNull(message = "must set if it as category or subCategory")
    boolean isParent;

    String parentId;

    public ActivatingCategoriesInHouseHold(String categoryId, boolean isActive, boolean isParent){
        this(categoryId, isActive, isParent,null);
    }

}
