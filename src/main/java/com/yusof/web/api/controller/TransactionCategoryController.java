package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.*;
import com.yusof.web.api.json.response.ReportResponse;
import com.yusof.web.api.json.response.TransactionCategoryResponse;
import com.yusof.web.entity.CategoryReportModel;
import com.yusof.web.service.TransactionCategoryDTO;
import com.yusof.web.service.TransactionCategoryService;
import com.yusof.web.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class TransactionCategoryController extends AbstractApiController {
    private final TransactionCategoryService transactionCategoryService;
    private final TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<TransactionCategoryResponse> createTransactionCategory(@RequestBody @Valid TransactionCategoryCreationRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        TransactionCategoryDTO transactionCategory = transactionCategoryService.createCategory(request.getName(), clientId);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionCategoryResponse.fromDTO(transactionCategory));
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteTransactionCategory(@RequestBody @Valid TransactionCategoryDeletionRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        transactionCategoryService.deleteTransactionCategory(request.getId(), clientId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update")
    public TransactionCategoryResponse updateTransactionCategory(@RequestBody @Valid TransactionCategoryUpdatingRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        TransactionCategoryDTO transactionCategoryDTO = transactionCategoryService.updateTransactionCategory(request.getName(), request.getId(), clientId);
        return TransactionCategoryResponse.fromDTO(transactionCategoryDTO);
    }

    @GetMapping("/show")
    public List<TransactionCategoryResponse> showTransactionCategories(HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        List<TransactionCategoryDTO> transactionCategories = transactionCategoryService.viewTransactionCategory(clientId);
        return transactionCategories.stream()
                .map(TransactionCategoryResponse::fromDTO)
                .toList();
    }

    @PostMapping("/report/income")
    public List<ReportResponse> getIncomeReport(@RequestBody @Valid IncomeReportRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        List<CategoryReportModel> transactions = transactionService.getIncomeReportByCategory(clientId, request.getStartDate(), request.getEndDate());
        return transactions.stream()
                .map(ReportResponse::fromModel)
                .toList();
    }

    @PostMapping("/report/expense")
    public List<ReportResponse> getExpenseReport(@RequestBody @Valid ExpenseReportRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        List<CategoryReportModel> transactions = transactionService.getExpenseReportByCategory(clientId, request.getStartDate(), request.getEndDate());
        return transactions.stream()
                .map(ReportResponse::fromModel)
                .toList();
    }
}
