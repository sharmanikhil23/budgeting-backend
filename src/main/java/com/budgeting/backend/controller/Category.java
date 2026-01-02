package com.budgeting.backend.controller;

import com.budgeting.backend.dto.in.ActivatingCategoriesInHouseHold;
import com.budgeting.backend.dto.out.CategoryWithSubResponse;
import com.budgeting.backend.entity.User;
import com.budgeting.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/category")
public class Category {

    private final CategoryService categoryService;

    @Autowired
    public Category(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/get-default-categories")
    public ResponseEntity<?> getCategories(){
        List<CategoryWithSubResponse> categoryResponse = categoryService.getAllCategories();
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @GetMapping("/get-categories/{houseHoldId}")
    public ResponseEntity<?> getCategories(@PathVariable String houseHoldId, @AuthenticationPrincipal User user ){
        List<CategoryWithSubResponse> categoryResponse = categoryService.getAllCategories(houseHoldId,user);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/set-categories/{houseHoldId}")
    public ResponseEntity<?> setCategories(List<ActivatingCategoriesInHouseHold> categories, @PathVariable String houseHoldId, @AuthenticationPrincipal User user){
        categoryService.activatingCategoriesInHouseHold(categories,user,houseHoldId);
        List<CategoryWithSubResponse> categoryResponse = categoryService.getAllCategories(houseHoldId,user);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }
}
