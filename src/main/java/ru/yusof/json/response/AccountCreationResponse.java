package ru.yusof.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yusof.service.AccountDTO;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountCreationResponse {
    private AccountDTO accountDTO;
}
