package com.yusof.web.api.json.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountCreationRequest {
    @NotBlank(message = "Account name is required")
    private String name;

    @NotNull
    @PositiveOrZero(message = "Balance must be zero or a positive number")
    private BigDecimal balance;
}
