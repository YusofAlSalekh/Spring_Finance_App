package com.yusof.web.api.json.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TransactionCreationRequest {
    @NotEmpty(message = "At least one transaction category must be provided.")
    private List<
            @Positive(message = "Transaction category mast be a positive number")
            @NotNull Integer> categoryIds;

    @NotNull
    @Positive(message = "Enter a positive number")
    private BigDecimal amount;

    @NotNull
    @Positive(message = "Enter a positive number")
    private Integer senderAccountId;

    @NotNull
    @Positive(message = "Enter a positive number")
    private Integer receiverAccountId;
}
