package com.budgeting.backend.repository.Custom;

import com.budgeting.backend.dto.out.CategoryWithSubResponse;
import com.budgeting.backend.dto.out.HouseholdCategoryResponse;

import java.util.List;


public interface HouseholdCategoryAggregationRepository {
    List<CategoryWithSubResponse> fetchByHousehold(String householdId);
}
