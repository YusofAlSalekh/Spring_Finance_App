package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.AccountsViewingRequest;
import com.yusof.web.api.json.response.AccountsViewingResponse;
import com.yusof.web.service.AccountDTO;
import com.yusof.web.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.yusof.web.api.controller.SessionUtils.getClientId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountsViewingController {
    private final AccountService accountService;

    @PostMapping("/account/show")
    public AccountsViewingResponse showAccounts(@RequestBody @Valid AccountsViewingRequest accountsViewingRequest, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        List<AccountDTO> listOfAccounts = accountService.viewAccount(clientId);
        return new AccountsViewingResponse(listOfAccounts);
    }
}
