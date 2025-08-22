package com.yusof.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "category")
public class TransactionCategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "client_id", nullable = false)
    private Integer clientId;

    @Override
    public String toString() {
        return "TransactionCategoryModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", clientId=" + clientId +
                '}';
    }
}