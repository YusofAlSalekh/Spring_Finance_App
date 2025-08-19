package ru.yusof.entity;

import javax.persistence.*;

@Entity
@Table(name = "category")
public class TransactionCategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "client_id")
    private Integer clientId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "TransactionCategoryModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", clientId=" + clientId +
                '}';
    }
}