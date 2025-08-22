package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.TransactionCategoryDeletionRequest;
import com.yusof.web.api.json.response.TransactionCategoryDeletionResponse;
import com.yusof.web.service.TransactionCategoryService;
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
public class TransactionCategoryDeletionController {
    private final TransactionCategoryService transactionCategoryService;

    @PostMapping("/category/delete")
    public TransactionCategoryDeletionResponse deleteTransactionCategory(@RequestBody @Valid TransactionCategoryDeletionRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        transactionCategoryService.deleteTransactionCategory(request.getId(), clientId);
        return new TransactionCategoryDeletionResponse("Transaction Category Deleted Successful");
    }
}
