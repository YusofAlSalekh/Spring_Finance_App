package com.yusof.web.api.json.response;

import com.yusof.web.service.AccountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountResponse {
    private Integer id;
    private String name;
    private BigDecimal balance;
    private Integer clientId;

    public static AccountResponse fromDTO(AccountDTO accountDTO) {
        return new AccountResponse(accountDTO.getId(), accountDTO.getName(), accountDTO.getBalance(), accountDTO.getClientId());
    }
}
