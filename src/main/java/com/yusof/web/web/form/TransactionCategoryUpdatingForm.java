package com.yusof.web.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransactionCategoryUpdatingForm {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull
    @Positive(message = "A positive number must be provided")
    private Integer id;
}
