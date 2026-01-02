package com.budgeting.backend.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrencyResponse {
    private String code;
    private String name;
    private String symbol;
}
