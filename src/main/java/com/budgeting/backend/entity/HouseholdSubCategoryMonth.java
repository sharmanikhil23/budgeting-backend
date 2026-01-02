package com.budgeting.backend.entity;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "household_subcategory_months")
@Data
@CompoundIndexes({
        @CompoundIndex(
                name = "unique_household_subcategory_month",
                def = "{'householdSubCategoryId':1, 'year':1, 'month':1}",
                unique = true
        )
})
public class HouseholdSubCategoryMonth {

    @Id
    private ObjectId id;

    private ObjectId householdId;

    private ObjectId householdSubCategoryId;

    private int year;
    private int month;

    private double budgetAmount;

    private double usedAmount;
}

