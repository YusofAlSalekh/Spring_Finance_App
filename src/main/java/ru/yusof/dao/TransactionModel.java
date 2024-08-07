package ru.yusof.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

public class TransactionModel {
    private int id;
    private Timestamp createdDate;
    private BigDecimal amount;
    private int senderAccountId;
    private int receiverAccountId;

    public TransactionModel(int id, Timestamp createdDate, BigDecimal amount, int senderAccountId, int receiverAccountId) {
        this.id = id;
        this.createdDate = createdDate;
        this.amount = amount;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionModel that = (TransactionModel) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getSenderAccountId() {
        return senderAccountId;
    }

    public int getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setSenderAccountId(int senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public void setReceiverAccountId(int receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }
}
