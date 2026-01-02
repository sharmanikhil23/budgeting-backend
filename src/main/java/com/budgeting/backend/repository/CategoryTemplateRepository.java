package com.budgeting.backend.repository;

import com.budgeting.backend.entity.CategoryTemplate;
import com.budgeting.backend.global.enums.DefaultCategories;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryTemplateRepository extends MongoRepository<CategoryTemplate, ObjectId> {
    Optional<CategoryTemplate> findByName(String name);

    Optional<CategoryTemplate> findByNameAndType(String name, DefaultCategories.CategoryType type );

    List<CategoryTemplate> findByIsDefault(boolean isDefault);

    List<CategoryTemplate> findByIsDefaultTrue();
}
