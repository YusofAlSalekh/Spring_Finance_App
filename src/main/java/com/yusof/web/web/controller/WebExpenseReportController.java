package com.yusof.web.web.controller;

import com.yusof.web.entity.CategoryAmountModel;
import com.yusof.web.service.TransactionService;
import com.yusof.web.web.form.ExpenseReportForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static com.yusof.web.api.controller.SessionUtils.getClientId;

@Controller
@RequiredArgsConstructor
public class WebExpenseReportController {
    private final TransactionService transactionService;

    @GetMapping("/transaction/expense")
    public String getExpenseReport(Model model) {
        model.addAttribute("form", new ExpenseReportForm());
        return "expenseReport";
    }

    @PostMapping("/transaction/expense")
    public String postExpenseReport(@ModelAttribute("form") @Valid ExpenseReportForm form,
                                    BindingResult bindingResult,
                                    HttpServletRequest request,
                                    Model model) {
        if (!bindingResult.hasErrors()) {
            Integer clientId = getClientId(request);

            List<CategoryAmountModel> transactions = transactionService.getExpenseReportByCategory(clientId,
                    form.getStartDate(),
                    form.getEndDate());

            model.addAttribute("transactions", transactions);
            return "expenseReport";
        }
        return "expenseReport";
    }
}
