package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.TransactionCreationRequest;
import com.yusof.web.api.json.response.TransactionCreationResponse;
import com.yusof.web.service.TransactionService;
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
public class TransactionCreationController {
    private final TransactionService transactionService;

    @PostMapping("/transaction/create")
    public TransactionCreationResponse createTransaction(@RequestBody @Valid TransactionCreationRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        TransactionCommandCreation command = TransactionCommandCreation.builder()
                .senderAccountId(request.getSenderAccountId())
                .clientId(clientId)
                .categoryIds(request.getCategoryIds())
                .amount(request.getAmount())
                .receiverAccountId(request.getReceiverAccountId())
                .build();

        transactionService.performTransaction(command);

        return new TransactionCreationResponse("Transaction created successfully");
    }
}
