package com.budgeting.backend.UnitTest.Service;


import com.budgeting.backend.dto.in.ActivatingCategoriesInHouseHold;
import com.budgeting.backend.dto.in.HouseHold;
import com.budgeting.backend.dto.in.SignUp;
import com.budgeting.backend.dto.out.CategoryWithSubResponse;
import com.budgeting.backend.dto.out.SubCategoryResponse;
import com.budgeting.backend.entity.User;
import com.budgeting.backend.global.enums.Currency;
import com.budgeting.backend.service.AuthenticationService;
import com.budgeting.backend.service.CategoryService;
import com.budgeting.backend.service.HouseHoldService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryTest {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    HouseHoldService houseHoldService;

    @Autowired
    CategoryService categoryService;

    User user;

    User primaryUser = null;

    String houseHoldId;

    @BeforeAll
    public void testSetUp() {
        // 2️⃣ Sign up the user
        SignUp sign = new SignUp();
        sign.setName("Nikhil Sharma");
        sign.setEmail("nikhil@example.com");
        sign.setPassword("Password123!");

        Map<String, Object> result = authenticationService.signUp(sign);

        String message = result.get("message").toString();
        Assertions.assertEquals("User created Successfully", message, "User should be created");

        // 3️⃣ Retrieve the created user
        primaryUser = authenticationService.getUserByEmail("nikhil@example.com");

        // 4️⃣ Create the HouseHold
        HouseHold houseHold = new HouseHold();
        houseHold.setName("Testing household");
        houseHold.setCurrency(Currency.CAD.getCode());

        ResponseEntity<?> houseHoldResult = houseHoldService.makeHouseHold(houseHold, primaryUser);
        Assertions.assertEquals(HttpStatus.CREATED, houseHoldResult.getStatusCode(), "HouseHold should be created");

        // 5️⃣ Extract houseHoldId safely
        Object body = houseHoldResult.getBody();
        Assertions.assertNotNull(body, "Response body should not be null");

        // Use a generic Map to avoid raw type warnings
        Map<String, String> responseBody = (Map<String, String>) body;
        this.houseHoldId = responseBody.get("houseHoldId");

        Assertions.assertNotNull(this.houseHoldId, "HouseHoldId should be present in response");
    }

    @AfterAll
    public void testTearDown(){
        if (primaryUser != null) {
            authenticationService.deleteUser(primaryUser.getId());
        }
    }

    @Test
    void shouldActivateSelectedCategoriesAndSubCategoriesForHousehold() throws Exception {
        // 1️⃣ Get defaults
        List<CategoryWithSubResponse> responseList = categoryService.getAllCategories();
        Assertions.assertFalse(responseList.isEmpty());

        // 2️⃣ Build Activation List
        List<ActivatingCategoriesInHouseHold> activating = new ArrayList<>();
        CategoryWithSubResponse cat0 = responseList.get(0);

        activating.add(new ActivatingCategoriesInHouseHold(cat0.getCategoryId(), true, true));
        for (SubCategoryResponse sub : cat0.getSubCategories()) {
            activating.add(new ActivatingCategoriesInHouseHold(sub.getId(), true, false, cat0.getCategoryId()));
        }

        // 3️⃣ Call Service
        categoryService.activatingCategoriesInHouseHold(activating, primaryUser, houseHoldId);

        // Give the DB a moment to finish indexing/writing
        Thread.sleep(1000);

        // 4️⃣ The Real Test: Fetch back using the aggregation
        List<CategoryWithSubResponse> finalResult = categoryService.getAllCategories(houseHoldId, primaryUser);

        // ⚡️ VERIFICATION
        Assertions.assertNotNull(finalResult, "The aggregation returned null!");
        Assertions.assertFalse(finalResult.isEmpty(), "No categories found for household: " + houseHoldId);

        CategoryWithSubResponse firstResult = finalResult.get(0);
        Assertions.assertNotNull(firstResult.getName(), "Category name should be populated from template");
        Assertions.assertFalse(firstResult.getSubCategories().isEmpty(), "Subcategories should be joined and populated");

        System.out.println("Success! Found " + finalResult.size() + " active categories.");
    }

}
