package com.budgeting.backend.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "household_category_months")
@Data
@CompoundIndexes({
        @CompoundIndex(
                name = "unique_household_category_month",
                def = "{'householdCategoryId':1, 'year':1, 'month':1}",
                unique = true
        )
})
public class HouseholdCategoryMonth {

    @Id
    private ObjectId id;

    private ObjectId householdId;

    private ObjectId householdCategoryId;

    private int year;   // 2025
    private int month;  // 1-12

    private double budgetAmount;

    private double usedAmount;

    private boolean isLocked; // optional (past months)
}
