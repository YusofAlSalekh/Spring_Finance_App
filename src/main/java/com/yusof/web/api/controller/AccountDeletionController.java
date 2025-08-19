package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.AccountDeletionRequest;
import com.yusof.web.api.json.response.AccountDeletionResponse;
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
public class AccountDeletionController {
    private final AccountService accountService;

    @PostMapping("/account/delete")
    public AccountDeletionResponse deleteAccount(@RequestBody @Valid AccountDeletionRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        accountService.deleteAccount(request.getAccountId(), clientId);
        return new AccountDeletionResponse("Account deleted successfully");
    }
}


