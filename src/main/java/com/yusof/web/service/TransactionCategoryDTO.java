package com.yusof.web.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionCategoryDTO {
    private int id;
    private String name;
    private int clientId;

    @Override
    public String toString() {
        return "TransactionCategoryDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", client_id=" + clientId +
                '}';
    }
}
