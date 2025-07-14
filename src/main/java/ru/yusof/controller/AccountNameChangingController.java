package ru.yusof.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.AccountNameChangingRequest;
import ru.yusof.json.AccountNameChangingResponse;
import ru.yusof.service.AccountDTO;
import ru.yusof.service.AccountService;

import static ru.yusof.controller.ValidationUtils.extractNotBlankString;
import static ru.yusof.controller.ValidationUtils.extractPositiveInteger;

@AllArgsConstructor
@Service("/account/update")
public class AccountNameChangingController implements SecureController<AccountNameChangingRequest, AccountNameChangingResponse> {
    private final AccountService accountService;

    @Override
    public AccountNameChangingResponse handle(AccountNameChangingRequest request, Integer userId) {
        String accountName = extractNotBlankString(request.getName());
        Integer accountId = extractPositiveInteger(request.getAccountId());

        AccountDTO updatedAccountName = accountService.updateAccountName(accountName, accountId, userId);
        return new AccountNameChangingResponse(updatedAccountName.getName());
    }

    @Override
    public Class<AccountNameChangingRequest> getRequestClass() {
        return AccountNameChangingRequest.class;
    }
}
