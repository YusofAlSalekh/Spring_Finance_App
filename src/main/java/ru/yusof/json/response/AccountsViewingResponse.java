package ru.yusof.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yusof.service.AccountDTO;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class AccountsViewingResponse {
    private List<AccountDTO> accounts;
}
