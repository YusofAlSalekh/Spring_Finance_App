package com.yusof.web.web.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransactionCategoryCreationForm {
    @NotBlank(message = "Name is required")
    private String name;
}
