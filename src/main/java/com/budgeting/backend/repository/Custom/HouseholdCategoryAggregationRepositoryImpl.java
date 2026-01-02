package com.budgeting.backend.repository.Custom;

import com.budgeting.backend.dto.out.CategoryWithSubResponse;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HouseholdCategoryAggregationRepositoryImpl implements HouseholdCategoryAggregationRepository {

    private final MongoTemplate mongoTemplate;

    public HouseholdCategoryAggregationRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

//    @Override
//    public List<CategoryWithSubResponse> fetchByHousehold(String householdId) {
//        ObjectId householdObjectId = new ObjectId(householdId);
//
//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.match(Criteria.where("householdId").is(householdObjectId).and("isActive").is(true)),
//                Aggregation.lookup("categories_template", "categoryTemplateId", "_id", "categoryTemplate"),
//                Aggregation.unwind("categoryTemplate", true),
//                Aggregation.lookup("house_hold_sub_categories", "_id", "parentCategoryID", "householdSubCategories"),
//                Aggregation.lookup("sub_categories_template", "householdSubCategories.subCategoryTemplateId", "_id", "subCategoryTemplates"),
//
//                Aggregation.addFields().addField("mergedSubCategories").withValue(
//                        VariableOperators.Map.itemsOf("householdSubCategories")
//                                .as("sc")
//                                .andApply(
//                                        ObjectOperators.MergeObjects.merge("$$sc")
//                                                .mergeWith(
//                                                        ArrayOperators.ArrayElemAt.arrayOf(
//                                                                ArrayOperators.Filter.filter("subCategoryTemplates")
//                                                                        .as("tpl")
//                                                                        .by(ComparisonOperators.Eq.valueOf("$$tpl._id")
//                                                                                .equalTo("$$sc.subCategoryTemplateId"))
//                                                        ).elementAt(0)
//                                                )
//                                )
//                ).build(),
//
//                Aggregation.project()
//                        .andExpression("toString(_id)").as("categoryId")
//                        .and("categoryTemplate.name").as("name")
//                        .and("categoryTemplate.type").as("type")
//                        .and(
//                                VariableOperators.Map.itemsOf("mergedSubCategories")
//                                        .as("msc")
//                                        .andApply(ctx -> new org.bson.Document()
//                                                .append("id", new org.bson.Document("$toString", "$$msc._id"))
//                                                .append("name", "$$msc.name")
//                                                .append("icon", "$$msc.icon")
//                                        )
//                        ).as("subCategories")
//        );
//
//        // 1️⃣ Get Raw Documents
//        List<org.bson.Document> rawDocs = mongoTemplate
//                .aggregate(aggregation, "house_hold_categories", org.bson.Document.class)
//                .getMappedResults();
//
//        // 2️⃣ Print the Raw JSON - THIS IS THE MOST IMPORTANT PART
//        System.out.println("DEBUG: Raw Mongo JSON: " + rawDocs.stream().map(org.bson.Document::toJson).toList());
//
//        // 3️⃣ Manually map to DTOs
//        return rawDocs.stream()
//                .map(doc -> mongoTemplate.getConverter().read(CategoryWithSubResponse.class, doc))
//                .toList();
//    }

    @Override
    public List<CategoryWithSubResponse> fetchByHousehold(String householdId) {
        ObjectId householdObjectId = new ObjectId(householdId);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("householdId").is(householdObjectId).and("isActive").is(true)),
                Aggregation.lookup("categories_template", "categoryTemplateId", "_id", "categoryTemplate"),
                Aggregation.unwind("categoryTemplate", true),
                Aggregation.lookup("house_hold_sub_categories", "_id", "parentCategoryID", "householdSubCategories"),
                Aggregation.lookup("sub_categories_template", "householdSubCategories.subCategoryTemplateId", "_id", "subCategoryTemplates"),

                // Merge templates into household data
                Aggregation.addFields().addField("mergedSubCategories").withValue(
                        VariableOperators.Map.itemsOf("householdSubCategories")
                                .as("sc")
                                .andApply(
                                        ObjectOperators.MergeObjects.merge("$$sc")
                                                .mergeWith(
                                                        ArrayOperators.ArrayElemAt.arrayOf(
                                                                ArrayOperators.Filter.filter("subCategoryTemplates")
                                                                        .as("tpl")
                                                                        .by(ComparisonOperators.Eq.valueOf("$$tpl._id")
                                                                                .equalTo("$$sc.subCategoryTemplateId"))
                                                        ).elementAt(0)
                                                )
                                )
                ).build(),

                // Project to DTO
                Aggregation.project()
                        .andExpression("toString(_id)").as("categoryId")
                        .and("categoryTemplate.name").as("name")
                        .and("categoryTemplate.type").as("type")
                        .and(
                                VariableOperators.Map.itemsOf("mergedSubCategories")
                                        .as("msc")
                                        .andApply(ctx -> new org.bson.Document()
                                                .append("id", new org.bson.Document("$toString", "$$msc._id"))
                                                .append("name", "$$msc.name")
                                                .append("icon", "$$msc.icon")
                                        )
                        ).as("subCategories")
        );

        // Now that the DTO has @NoArgsConstructor, this will work perfectly
        return mongoTemplate.aggregate(aggregation, "house_hold_categories", CategoryWithSubResponse.class)
                .getMappedResults();
    }
}