package com.budgeting.backend.entity;

import com.budgeting.backend.dto.in.ActivatingCategoriesInHouseHold;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "house_hold_sub_categories")
@Data
@CompoundIndexes({
        @CompoundIndex(
                name = "unique_household_sub_category",
                def = "{'householdId': 1, 'parentCategoryID':1, 'subCategoryTemplateId':1}",
                unique = true
        )
})
public class HouseHoldSubCategory {

    @Id
    private ObjectId id;

    private ObjectId householdId;

    private ObjectId parentCategoryID;

    private ObjectId subCategoryTemplateId;

    private boolean isActive;

    public HouseHoldSubCategory(ActivatingCategoriesInHouseHold temp, String householdId){
        this.householdId =new ObjectId(householdId);
        this.isActive = temp.isActive();
        this.subCategoryTemplateId = new ObjectId(temp.getCategoryId());
        this.parentCategoryID = new ObjectId(temp.getParentId());
    }
}

