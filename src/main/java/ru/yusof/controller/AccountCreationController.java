package ru.yusof.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.AccountCreationRequest;
import ru.yusof.json.AccountCreationResponse;
import ru.yusof.service.AccountDTO;
import ru.yusof.service.AccountService;

import java.math.BigDecimal;

import static ru.yusof.controller.ValidationUtils.extractNotBlankString;
import static ru.yusof.controller.ValidationUtils.extractPositiveBigDecimal;

@Service("/account/create")
@RequiredArgsConstructor
public class AccountCreationController implements SecureController<AccountCreationRequest, AccountCreationResponse> {
    private final AccountService accountService;

    @Override
    public AccountCreationResponse handle(AccountCreationRequest request, Integer userId) {
        String name = extractNotBlankString(request.getName());
        BigDecimal balance = extractPositiveBigDecimal(request.getBalance());
        AccountDTO accountDTO = accountService.createAccount(name, balance, userId);

        return new AccountCreationResponse(accountDTO);
    }

    @Override
    public Class<AccountCreationRequest> getRequestClass() {
        return AccountCreationRequest.class;
    }
}
