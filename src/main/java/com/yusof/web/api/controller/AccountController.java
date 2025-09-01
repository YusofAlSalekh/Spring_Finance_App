package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.AccountCreationRequest;
import com.yusof.web.api.json.request.AccountDeletionRequest;
import com.yusof.web.api.json.request.AccountNameChangingRequest;
import com.yusof.web.api.json.response.AccountResponse;
import com.yusof.web.security.CustomUserDetails;
import com.yusof.web.service.AccountDTO;
import com.yusof.web.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController extends AbstractApiController {
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody @Valid AccountCreationRequest request,
                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Integer clientId = customUserDetails.getId();

        AccountDTO accountDTO = accountService.createAccount(request.getName(), request.getBalance(), clientId);
        return ResponseEntity.status(HttpStatus.CREATED).body(AccountResponse.fromDTO(accountDTO));
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteAccount(@RequestBody @Valid AccountDeletionRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Integer clientId = customUserDetails.getId();

        accountService.deleteAccount(request.getAccountId(), clientId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update")
    public AccountResponse changeAccountName(@RequestBody @Valid AccountNameChangingRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Integer clientId = customUserDetails.getId();

        AccountDTO accountDTO = accountService.updateAccountName(request.getName(), request.getAccountId(), clientId);
        return AccountResponse.fromDTO(accountDTO);
    }

    @GetMapping("/show")
    public List<AccountResponse> showAccounts(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Integer clientId = customUserDetails.getId();

        List<AccountDTO> listOfAccounts = accountService.viewAccount(clientId);

        return listOfAccounts.stream()
                .map(AccountResponse::fromDTO)
                .toList();
    }
}
