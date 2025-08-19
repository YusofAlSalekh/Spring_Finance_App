package com.yusof.web.api.json.response;

import com.yusof.web.service.AccountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountCreationResponse {
    private AccountDTO accountDTO;
}
