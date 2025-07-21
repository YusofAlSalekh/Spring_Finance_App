package ru.yusof.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "transaction")
public class TransactionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "amount")
    private BigDecimal amount;

    @ManyToOne()
    @JoinColumn
    private AccountModel sender;

    @ManyToOne()
    @JoinColumn
    private AccountModel receiver;

    @ManyToMany
    @JoinTable(name = "transaction_to_category",
            joinColumns = @JoinColumn(name = "transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<TransactionCategoryModel> categories;

    public TransactionModel() {
    }

    public TransactionModel(Integer id, Timestamp createdDate, BigDecimal amount, AccountModel sender, AccountModel receiver) {
        this.id = id;
        this.createdDate = createdDate;
        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransactionModel that = (TransactionModel) o;
        return Objects.equals(id, that.id) && Objects.equals(createdDate, that.createdDate) && Objects.equals(amount, that.amount) && Objects.equals(sender, that.sender) && Objects.equals(receiver, that.receiver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, amount, sender, receiver);
    }

    @Override
    public String toString() {
        return "TransactionModel{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", amount=" + amount +
                ", senderAccountId=" + sender +
                ", receiverAccountId=" + receiver +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AccountModel getSender() {
        return sender;
    }

    public void setSender(AccountModel senderAccountId) {
        this.sender = senderAccountId;
    }

    public AccountModel getReceiver() {
        return receiver;
    }

    public void setReceiver(AccountModel receiverAccountId) {
        this.receiver = receiverAccountId;
    }

    public List<TransactionCategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<TransactionCategoryModel> categories) {
        this.categories = categories;
    }
}
