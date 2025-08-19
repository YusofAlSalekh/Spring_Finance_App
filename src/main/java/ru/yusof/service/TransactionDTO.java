package ru.yusof.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {
    private int id;
    private LocalDateTime createdDate;
    private BigDecimal amount;
    private int senderAccountId;
    private int receiverAccountId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(int senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public int getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(int receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

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