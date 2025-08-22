package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.TransactionCategoryUpdatingRequest;
import com.yusof.web.api.json.response.TransactionCategoryUpdatingResponse;
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
public class TransactionCategoryUpdatingController {
    private final TransactionCategoryService transactionCategoryService;

    @PostMapping("/category/update")
    public TransactionCategoryUpdatingResponse updateTransactionCategory(@RequestBody @Valid TransactionCategoryUpdatingRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        TransactionCategoryDTO transactionCategoryDTO = transactionCategoryService.updateTransactionCategory(request.getName(), request.getId(), clientId);
        return new TransactionCategoryUpdatingResponse(transactionCategoryDTO.getId(), transactionCategoryDTO.getName());
    }
}
