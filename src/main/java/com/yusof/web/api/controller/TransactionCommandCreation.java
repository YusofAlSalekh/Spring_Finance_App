package com.yusof.web.api.controller;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
public class TransactionCommandCreation {
    private Integer senderAccountId;
    private Integer receiverAccountId;
    private Integer clientId;
    private BigDecimal amount;
    private List<Integer> categoryIds;
}
