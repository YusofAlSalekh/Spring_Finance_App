package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.AccountNameChangingRequest;
import com.yusof.web.api.json.response.AccountNameChangingResponse;
import com.yusof.web.service.AccountDTO;
import com.yusof.web.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.yusof.web.api.controller.SessionUtils.getClientId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountNameChangingController {
    private final AccountService accountService;

    @PostMapping("/account/update")
    public AccountNameChangingResponse changeAccountName(@RequestBody @Valid AccountNameChangingRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        AccountDTO updatedAccountName = accountService.updateAccountName(request.getName(), request.getAccountId(), clientId);
        return new AccountNameChangingResponse(updatedAccountName.getId(), updatedAccountName.getName());
    }
}
