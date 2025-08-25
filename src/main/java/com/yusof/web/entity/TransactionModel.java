package com.yusof.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class TransactionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AccountModel sender;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AccountModel receiver;

    @ManyToMany
    @JoinTable(name = "transaction_to_category",
            joinColumns = @JoinColumn(name = "transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<TransactionCategoryModel> categories;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransactionModel that = (TransactionModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
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
}
