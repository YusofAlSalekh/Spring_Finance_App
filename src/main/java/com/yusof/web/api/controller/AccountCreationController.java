package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.AccountCreationRequest;
import com.yusof.web.api.json.response.AccountCreationResponse;
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
public class AccountCreationController {
    private final AccountService accountService;

    @PostMapping("/account/create")
    public AccountCreationResponse createAccount(@RequestBody @Valid AccountCreationRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        AccountDTO accountDTO = accountService.createAccount(request.getName(), request.getBalance(), clientId);
        return new AccountCreationResponse(accountDTO);
    }
}
