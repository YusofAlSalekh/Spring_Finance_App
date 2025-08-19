package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.TransactionCategoryCreationRequest;
import com.yusof.web.api.json.response.TransactionCategoryCreationResponse;
import com.yusof.web.service.TransactionCategoryDTO;
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
public class TransactionCategoryCreationController {
    private final TransactionCategoryService transactionCategoryService;

    @PostMapping("/category/create")
    public TransactionCategoryCreationResponse createTransactionCategory(@RequestBody @Valid TransactionCategoryCreationRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        TransactionCategoryDTO transactionCategory = transactionCategoryService.createCategory(request.getName(), clientId);
        return new TransactionCategoryCreationResponse(transactionCategory);
    }
}
