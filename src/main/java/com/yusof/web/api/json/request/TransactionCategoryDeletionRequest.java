package com.yusof.web.api.json.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransactionCategoryDeletionRequest {
    @NotNull
    @Positive(message = "A positive number must be provided")
    private Integer id;
}
