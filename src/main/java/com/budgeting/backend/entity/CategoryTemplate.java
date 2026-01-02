package com.budgeting.backend.entity;

import com.budgeting.backend.global.enums.DefaultCategories;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * It will act like a template for user so if user does not have one they like provided
 * by us they can add new one and will be shared among others too but abiding the rules of
 * mutual
 *
 * */
@Document(collection = "categories_template")
@CompoundIndexes({
        @CompoundIndex(
                name = "unique_categories_template",
                def = "{'name':1, 'type':1 }",
                unique = true
        )
})
@Data
public class CategoryTemplate {
    @Id
    private ObjectId id;

    private String name;

    private DefaultCategories.CategoryType type; // EXPENSE, INCOME, SAVING

    private boolean isDefault;
}
