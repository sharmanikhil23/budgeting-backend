package com.budgeting.backend.configuration;

import com.budgeting.backend.entity.CategoryTemplate;
import com.budgeting.backend.entity.SubCategoryTemplate;
import com.budgeting.backend.global.enums.DefaultCategories;
import com.budgeting.backend.repository.CategoryTemplateRepository;
import com.budgeting.backend.repository.SubCategoryTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(1)
public class StartupRunner implements ApplicationRunner {

    private final CategoryTemplateRepository categoryTemplateRepository;
    private final SubCategoryTemplateRepository subCategoryTemplateRepository;

    @Autowired
    public StartupRunner(CategoryTemplateRepository categoryTemplateRepository,
                         SubCategoryTemplateRepository subCategoryTemplateRepository){
        this.categoryTemplateRepository = categoryTemplateRepository;
        this.subCategoryTemplateRepository = subCategoryTemplateRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createExpenseCategories();
        createIncomeCategories();
        createSavingsCategories();
    }

    // ====================== EXPENSES ======================
    private void createExpenseCategories() {
        List<SubCategoryTemplate> categories = new ArrayList<>();

        DefaultCategories.EXPENSES.forEach((parentName, children) -> {

            CategoryTemplate parent = categoryTemplateRepository
                    .findByNameAndType(parentName.trim().toLowerCase(), DefaultCategories.CategoryType.EXPENSE)
                    .orElse(null);
            boolean newCategory = false;

            if(parent == null){
                parent = new CategoryTemplate();
                parent.setName(parentName.trim().toLowerCase());
                parent.setType(DefaultCategories.CategoryType.EXPENSE);
                parent.setDefault(true);
                newCategory = true;
                parent = categoryTemplateRepository.save(parent);
            }

            for (DefaultCategories.CategoryWithIcon childName : children) {
                SubCategoryTemplate child = null;

                if(!newCategory){
                    child = subCategoryTemplateRepository.findByNameAndTypeAndParentCategoryTemplateId(
                            childName.getName().trim().toLowerCase(),
                            DefaultCategories.CategoryType.EXPENSE,
                            parent.getId()
                    ).orElse(null);
                    if(child != null) continue;
                }

                child = new SubCategoryTemplate();
                child.setName(childName.getName().trim().toLowerCase());
                child.setIcon(childName.getIcon());
                child.setDefault(true);
                child.setParentCategoryTemplateId(parent.getId());
                child.setType(DefaultCategories.CategoryType.EXPENSE);

                categories.add(child);
            }
        });

        if(!categories.isEmpty()) {
            subCategoryTemplateRepository.saveAll(categories);
        }
    }

    // ====================== INCOME ======================
    private void createIncomeCategories() {
        List<SubCategoryTemplate> categories = new ArrayList<>();

        CategoryTemplate parent = categoryTemplateRepository
                .findByNameAndType("Income", DefaultCategories.CategoryType.INCOME)
                .orElse(null);

        if(parent == null){
            parent = new CategoryTemplate();
            parent.setName("Income".toLowerCase());
            parent.setType(DefaultCategories.CategoryType.INCOME);
            parent.setDefault(true);
            parent = categoryTemplateRepository.save(parent);
        }

        for (DefaultCategories.CategoryWithIcon income : DefaultCategories.INCOME) {
            SubCategoryTemplate child = subCategoryTemplateRepository
                    .findByNameAndTypeAndParentCategoryTemplateId(
                            income.getName().trim().toLowerCase(),
                            DefaultCategories.CategoryType.INCOME,
                            parent.getId()
                    ).orElse(null);

            if(child != null) continue;

            child = new SubCategoryTemplate();
            child.setName(income.getName().trim().toLowerCase());
            child.setIcon(income.getIcon());
            child.setDefault(true);
            child.setParentCategoryTemplateId(parent.getId());
            child.setType(DefaultCategories.CategoryType.INCOME);

            categories.add(child);
        }

        if(!categories.isEmpty()) {
            subCategoryTemplateRepository.saveAll(categories);
        }
    }

    // ====================== SAVINGS ======================
    private void createSavingsCategories() {
        List<SubCategoryTemplate> categories = new ArrayList<>();

        CategoryTemplate parent = categoryTemplateRepository
                .findByNameAndType("Savings", DefaultCategories.CategoryType.SAVING)
                .orElse(null);

        if(parent == null){
            parent = new CategoryTemplate();
            parent.setName("Savings".toLowerCase());
            parent.setType(DefaultCategories.CategoryType.SAVING);
            parent.setDefault(true);
            parent = categoryTemplateRepository.save(parent);
        }

        for (DefaultCategories.CategoryWithIcon saving : DefaultCategories.SAVINGS) {
            SubCategoryTemplate child = subCategoryTemplateRepository
                    .findByNameAndTypeAndParentCategoryTemplateId(
                            saving.getName().trim().toLowerCase(),
                            DefaultCategories.CategoryType.SAVING,
                            parent.getId()
                    ).orElse(null);

            if(child != null) continue;

            child = new SubCategoryTemplate();
            child.setName(saving.getName().trim().toLowerCase());
            child.setIcon(saving.getIcon());
            child.setDefault(true);
            child.setParentCategoryTemplateId(parent.getId());
            child.setType(DefaultCategories.CategoryType.SAVING);

            categories.add(child);
        }

        if(!categories.isEmpty()) {
            subCategoryTemplateRepository.saveAll(categories);
        }
    }
}
