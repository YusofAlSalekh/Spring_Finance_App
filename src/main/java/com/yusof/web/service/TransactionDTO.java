package com.yusof.web.service;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class TransactionDTO {
    private int id;
    private LocalDateTime createdDate;
    private BigDecimal amount;
    private int senderAccountId;
    private int receiverAccountId;

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", amount=" + amount +
                ", senderAccountId=" + senderAccountId +
                ", receiverAccountId=" + receiverAccountId +
                '}';
    }
}