package com.budgeting.backend.controller;

import com.budgeting.backend.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;


@RestController
@RequestMapping("/public")
public class Public {
    CurrencyService currencyService;

    @Autowired
    public Public(CurrencyService currencyService){
        this.currencyService = currencyService;
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck(){
        HashMap<String, String> result= new HashMap<>();
        result.put("Message","Health check Passed");

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/currencies")
    public ResponseEntity<?> getCurrencies() {
        return currencyService.getAllCurrencies();
    }
}
