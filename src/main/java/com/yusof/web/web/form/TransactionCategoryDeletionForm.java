package com.yusof.web.web.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransactionCategoryDeletionForm {
    @NotNull
    @Positive(message = "A positive number must be provided")
    private Integer id;
}
