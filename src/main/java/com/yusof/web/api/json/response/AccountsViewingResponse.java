package com.yusof.web.api.json.response;

import com.yusof.web.service.AccountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AccountsViewingResponse {
    private List<AccountDTO> accounts;
}
