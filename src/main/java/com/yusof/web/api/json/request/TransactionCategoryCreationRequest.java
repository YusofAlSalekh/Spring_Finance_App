package com.yusof.web.api.json.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransactionCategoryCreationRequest {
    @NotBlank(message = "Name is required")
    private String name;
}
