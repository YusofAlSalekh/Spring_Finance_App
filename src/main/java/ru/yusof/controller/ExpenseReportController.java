package ru.yusof.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.entity.CategoryAmountModel;
import ru.yusof.json.request.ExpenseReportRequest;
import ru.yusof.json.response.ExpenseReportResponse;
import ru.yusof.service.TransactionService;

import java.util.List;

@Service("/transaction/expense")
@AllArgsConstructor
public class ExpenseReportController implements SecureController<ExpenseReportRequest, ExpenseReportResponse> {
    private final TransactionService transactionService;

    @Override
    public ExpenseReportResponse handle(ExpenseReportRequest request, Integer userId) {
        List<CategoryAmountModel> transactions = transactionService.getExpenseReportByCategory(userId, request.getStartDate(), request.getEndDate());
        return new ExpenseReportResponse(transactions);
    }

    @Override
    public Class<ExpenseReportRequest> getRequestClass() {
        return ExpenseReportRequest.class;
    }
}
