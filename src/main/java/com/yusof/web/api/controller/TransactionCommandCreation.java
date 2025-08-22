package com.yusof.web.api.controller;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class TransactionCommandCreation {
    @NotNull
    @Positive
    private Integer senderAccountId;

    @NotNull
    @Positive
    private Integer receiverAccountId;

    private Integer clientId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotEmpty
    private List<@Positive @NotNull Integer> categoryIds;
}
