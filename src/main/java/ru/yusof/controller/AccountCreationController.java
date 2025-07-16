package ru.yusof.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.request.AccountCreationRequest;
import ru.yusof.json.response.AccountCreationResponse;
import ru.yusof.service.AccountDTO;
import ru.yusof.service.AccountService;

import java.math.BigDecimal;

import static ru.yusof.controller.ValidationUtils.validateNotBlankString;
import static ru.yusof.controller.ValidationUtils.validatePositiveBigDecimal;

@Service("/account/create")
@RequiredArgsConstructor
public class AccountCreationController implements SecureController<AccountCreationRequest, AccountCreationResponse> {
    private final AccountService accountService;

    @Override
    public AccountCreationResponse handle(AccountCreationRequest request, Integer userId) {
        String name = validateNotBlankString(request.getName());
        BigDecimal balance = validatePositiveBigDecimal(request.getBalance());
        AccountDTO accountDTO = accountService.createAccount(name, balance, userId);

        return new AccountCreationResponse(accountDTO);
    }

    @Override
    public Class<AccountCreationRequest> getRequestClass() {
        return AccountCreationRequest.class;
    }
}
