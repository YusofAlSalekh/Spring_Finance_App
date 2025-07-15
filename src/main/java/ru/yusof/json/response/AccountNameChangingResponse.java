package ru.yusof.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yusof.service.AccountDTO;

@AllArgsConstructor
@Data
public class AccountNameChangingResponse {
    private String name;
}
