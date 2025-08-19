package com.yusof.web.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountCreationForm {
    @NotBlank
    private String name;

    @NotNull
    @PositiveOrZero
    private BigDecimal balance;
}
