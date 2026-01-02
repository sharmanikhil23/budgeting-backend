package com.budgeting.backend.entity;

import com.budgeting.backend.global.enums.DefaultCategories;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sub_categories_template")
@Data
@CompoundIndexes({
        @CompoundIndex(
                name = "unique_sub_categories_template",
                def = "{'name':1, 'type':1, 'parentCategoryTemplateId':1}",
                unique = true
        )
})
public class SubCategoryTemplate {
    @Id
    private ObjectId id;

    private ObjectId parentCategoryTemplateId;

    private String name;

    private String icon;

    private DefaultCategories.CategoryType type; // EXPENSE, INCOME, SAVING

    private boolean isDefault;
}
