package ru.yusof.Web;

import ru.yusof.dao.CategoryAmountModel;
import ru.yusof.service.TransactionService;
import ru.yusof.view.SpringContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class ExpenseReportServlet extends BaseServlet {
    private final TransactionService transactionService;

    public ExpenseReportServlet() {
        this.transactionService = SpringContext.getContext().getBean(TransactionService.class);
    }

    @Override
    protected void doGetInternal(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws Exception {
        PrintWriter writer = resp.getWriter();

        LocalDate[] range = parseDateRange(req, resp);
        if (range == null) {
            return;
        }
        LocalDate startDate = range[0];
        LocalDate endDate = range[1];

        List<CategoryAmountModel> transactions = transactionService.getExpenseReportByCategory(userId, startDate, endDate);
        resp.setStatus(HttpServletResponse.SC_OK);
        writer.write(transactions.toString());
    }
}
