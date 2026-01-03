package com.budgeting.backend.controller;

import com.budgeting.backend.entity.User;
import com.budgeting.backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class Transaction {
    private TransactionService transactionService;
    @Autowired
    public Transaction(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody com.budgeting.backend.dto.in.Transaction transaction,
                                 @AuthenticationPrincipal User user ){
        return transactionService.save(transaction,user);
    }
}
