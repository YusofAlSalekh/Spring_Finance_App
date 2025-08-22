package com.yusof.web.api.json.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AccountNameChangingResponse {
    private Integer id;
    private String name;
}
