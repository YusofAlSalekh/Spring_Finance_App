package ru.yusof.json;

import lombok.Data;

@Data
public class AccountNameChangingRequest {
    private String name;
    private Integer accountId;
}
