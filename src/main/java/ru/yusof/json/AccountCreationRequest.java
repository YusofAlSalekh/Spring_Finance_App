package ru.yusof.json;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountCreationRequest {
    private String name;
    private BigDecimal balance;
}
