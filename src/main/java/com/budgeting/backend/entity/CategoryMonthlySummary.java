package com.budgeting.backend.entity;

import com.budgeting.backend.global.enums.DefaultCategories;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "category_monthly_summary")
@Data
@CompoundIndexes({
        @CompoundIndex(
                name = "unique_monthly_summary",
                def = "{'householdId': 1, 'categoryId': 1, 'subCategoryId': 1, 'year': 1, 'month': 1}",
                unique = true
        )
})
public class CategoryMonthlySummary {

    @Id
    private String id;

    private ObjectId householdId;

    private ObjectId categoryId;

    private ObjectId subCategoryId;

    private int year;

    private int month;

    private DefaultCategories.CategoryType type;

    private BigDecimal totalAmount;

    private Instant updatedAt;
}

