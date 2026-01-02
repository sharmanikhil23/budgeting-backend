package com.budgeting.backend.service;

import com.budgeting.backend.dto.out.CurrencyResponse;
import com.budgeting.backend.global.enums.Currency;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CurrencyService {

    public ResponseEntity<?> getAllCurrencies() {
        List<CurrencyResponse> currencyResponses= Arrays.stream(Currency.values())
                .map(c -> new CurrencyResponse(
                        c.getCode(),
                        c.getName(),
                        c.getSymbol()
                ))
                .toList();

        return new ResponseEntity<>(currencyResponses, HttpStatus.OK);
    }
}
