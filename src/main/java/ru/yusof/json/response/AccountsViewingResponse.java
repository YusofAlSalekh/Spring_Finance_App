package ru.yusof.json.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yusof.service.AccountDTO;

import java.util.List;

@Data
@AllArgsConstructor
public class AccountsViewingResponse {
    private List<AccountDTO> accounts;
}
