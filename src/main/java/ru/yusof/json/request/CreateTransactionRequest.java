package ru.yusof.json.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateTransactionRequest {
    private List<Integer> categoryIds;
    private BigDecimal amount;
    private int senderAccountId;
    private int receiverAccountId;
}
