package ru.yusof.json.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yusof.service.AccountDTO;

@Data
@AllArgsConstructor
public class AccountCreationResponse {
    private AccountDTO accountDTO;
}
