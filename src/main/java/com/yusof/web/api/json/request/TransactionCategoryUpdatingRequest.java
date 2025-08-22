package com.yusof.web.api.json.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransactionCategoryUpdatingRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull
    @Positive(message = "A positive number must be provided")
    private Integer id;
}
