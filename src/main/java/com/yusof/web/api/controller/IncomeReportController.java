package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.IncomeReportRequest;
import com.yusof.web.api.json.response.IncomeReportResponse;
import com.yusof.web.entity.CategoryAmountModel;
import com.yusof.web.service.TransactionService;
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
public class IncomeReportController {
    private final TransactionService transactionService;

    @PostMapping("/transaction/income")
    public IncomeReportResponse getIncomeReport(@RequestBody @Valid IncomeReportRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        List<CategoryAmountModel> transactions = transactionService.getIncomeReportByCategory(clientId, request.getStartDate(), request.getEndDate());
        return new IncomeReportResponse(transactions);
    }
}
