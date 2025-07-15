package ru.yusof.json.request;

import lombok.Data;

@Data
public class TransactionCategoryUpdatingRequest {
    private String name;
    private Integer id;
}
