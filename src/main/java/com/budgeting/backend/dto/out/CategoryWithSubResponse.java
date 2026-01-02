package com.budgeting.backend.dto.out;

import com.budgeting.backend.global.enums.DefaultCategories;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryWithSubResponse {
    private String categoryId;
    private String name;
    private DefaultCategories.CategoryType type;
    private List<SubCategoryResponse> subCategories;
}
