package com.budgeting.backend.entity;

import com.budgeting.backend.dto.in.ActivatingCategoriesInHouseHold;
import com.budgeting.backend.global.enums.DefaultCategories;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "house_hold_categories")
@Data
@CompoundIndexes({
        @CompoundIndex(
                name = "unique_household_Category",
                def = "{'householdId': 1, 'categoryTemplateId':1 }",
                unique = true
        )
})
public class HouseHoldCategory {
    @Id
    private ObjectId id;

    private ObjectId householdId;

    private ObjectId categoryTemplateId;

    private boolean isActive;

    public HouseHoldCategory(ActivatingCategoriesInHouseHold temp, ObjectId householdId){
        this.householdId = householdId;
        this.isActive = temp.isActive();
        this.categoryTemplateId = new ObjectId(temp.getCategoryId());
    }
}
