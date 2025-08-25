package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.TransactionCreationRequest;
import com.yusof.web.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransactionController extends AbstractApiController {
    private final TransactionService transactionService;

    @PostMapping("/transaction/create")
    public ResponseEntity<Void> createTransaction(@RequestBody @Valid TransactionCreationRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        TransactionCommandCreation command = TransactionCommandCreation.builder()
                .senderAccountId(request.getSenderAccountId())
                .clientId(clientId)
                .categoryIds(request.getCategoryIds())
                .amount(request.getAmount())
                .receiverAccountId(request.getReceiverAccountId())
                .build();

        transactionService.performTransaction(command);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
