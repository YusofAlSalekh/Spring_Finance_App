package ru.yusof.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.AccountDeletionRequest;
import ru.yusof.json.AccountDeletionResponse;
import ru.yusof.service.AccountService;

import static ru.yusof.controller.ValidationUtils.extractPositiveInteger;

@RequiredArgsConstructor
@Service("/account/delete")
public class AccountDeletionController implements SecureController<AccountDeletionRequest, AccountDeletionResponse> {
    private final AccountService accountService;

    @Override
    public AccountDeletionResponse handle(AccountDeletionRequest request, Integer userId) {
        Integer accountId = extractPositiveInteger(request.getAccountId());
        accountService.deleteAccount(accountId, userId);
        return new AccountDeletionResponse("Account deleted successfully");
    }

    @Override
    public Class<AccountDeletionRequest> getRequestClass() {
        return AccountDeletionRequest.class;
    }
}
