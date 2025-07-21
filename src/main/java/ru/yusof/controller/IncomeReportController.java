package ru.yusof.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.entity.CategoryAmountModel;
import ru.yusof.json.request.IncomeReportRequest;
import ru.yusof.json.response.IncomeReportResponse;
import ru.yusof.service.TransactionService;

import java.util.List;

@AllArgsConstructor
@Service("/transaction/income")
public class IncomeReportController implements SecureController<IncomeReportRequest, IncomeReportResponse> {
    private final TransactionService transactionService;

    @Override
    public IncomeReportResponse handle(IncomeReportRequest request, Integer userId) {
        List<CategoryAmountModel> transactions = transactionService.getIncomeReportByCategory(userId, request.getStartDate(), request.getEndDate());
        return new IncomeReportResponse(transactions);
    }

    @Override
    public Class<IncomeReportRequest> getRequestClass() {
        return IncomeReportRequest.class;
    }
}
