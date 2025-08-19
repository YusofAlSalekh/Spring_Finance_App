package ru.yusof.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.request.AccountDeletionRequest;
import ru.yusof.json.response.AccountDeletionResponse;
import ru.yusof.service.AccountService;

import static ru.yusof.controller.ValidationUtils.validatePositiveInteger;

@RequiredArgsConstructor
@Service("/account/delete")
public class AccountDeletionController implements SecureController<AccountDeletionRequest, AccountDeletionResponse> {
    private final AccountService accountService;

    @Override
    public AccountDeletionResponse handle(AccountDeletionRequest request, Integer userId) {
        validatePositiveInteger(request.getAccountId());
        Integer accountId = request.getAccountId();

        accountService.deleteAccount(accountId, userId);
        return new AccountDeletionResponse("Account deleted successfully");
    }

    @Override
    public Class<AccountDeletionRequest> getRequestClass() {
        return AccountDeletionRequest.class;
    }
}
