package com.budgeting.backend.repository;

import com.budgeting.backend.entity.SubCategoryTemplate;
import com.budgeting.backend.global.enums.DefaultCategories;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SubCategoryTemplateRepository extends MongoRepository<SubCategoryTemplate, ObjectId> {

    // Finds by name, type, and parent ID
    Optional<SubCategoryTemplate> findByNameAndTypeAndParentCategoryTemplateId(
            String name,
            DefaultCategories.CategoryType type,
            ObjectId parentCategoryTemplateId
    );

    // Finds all default subcategories
    List<SubCategoryTemplate> findByIsDefault(boolean isDefault);

    // Shortcut to get all subcategories where isDefault == true
    List<SubCategoryTemplate> findByIsDefaultTrue();
}
