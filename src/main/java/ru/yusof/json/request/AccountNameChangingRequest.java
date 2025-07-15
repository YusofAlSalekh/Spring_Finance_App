package ru.yusof.json.request;

import lombok.Data;

@Data
public class AccountNameChangingRequest {
    private String name;
    private Integer accountId;
}
