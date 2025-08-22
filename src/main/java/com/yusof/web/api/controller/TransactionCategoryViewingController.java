package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.TransactionCategoryViewingRequest;
import com.yusof.web.api.json.response.TransactionCategoryViewingResponse;
import com.yusof.web.service.TransactionCategoryDTO;
import com.yusof.web.service.TransactionCategoryService;
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
public class TransactionCategoryViewingController {
    private final TransactionCategoryService transactionCategoryService;

    @PostMapping("/category/show")
    public TransactionCategoryViewingResponse showTransactionCategories(@RequestBody @Valid TransactionCategoryViewingRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        List<TransactionCategoryDTO> transactionCategory = transactionCategoryService.viewTransactionCategory(clientId);
        return new TransactionCategoryViewingResponse(transactionCategory);
    }
}
