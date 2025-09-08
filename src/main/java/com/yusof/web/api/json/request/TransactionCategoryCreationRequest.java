package com.yusof.web.api.json.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionCategoryCreationRequest {
    @NotBlank(message = "Name is required")
    private String name;
}
