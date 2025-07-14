package ru.yusof.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.AccountsViewingRequest;
import ru.yusof.json.AccountsViewingResponse;
import ru.yusof.service.AccountDTO;
import ru.yusof.service.AccountService;

import java.util.List;

@RequiredArgsConstructor
@Service("/account/show")
public class AccountsViewingController implements SecureController<AccountsViewingRequest, AccountsViewingResponse> {
    final private AccountService accountService;

    @Override
    public AccountsViewingResponse handle(AccountsViewingRequest request, Integer userId) {
        List<AccountDTO> listOfAccounts = accountService.viewAccount(userId);
        return new AccountsViewingResponse(listOfAccounts);
    }

    @Override
    public Class<AccountsViewingRequest> getRequestClass() {
        return AccountsViewingRequest.class;
    }
}
