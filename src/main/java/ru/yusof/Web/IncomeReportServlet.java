package ru.yusof.Web;

import ru.yusof.dao.CategoryAmountModel;
import ru.yusof.service.TransactionService;
import ru.yusof.view.SpringContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class IncomeReportServlet extends BaseServlet {
    private final TransactionService transactionService;

    public IncomeReportServlet() {
        this.transactionService = SpringContext.getContext().getBean(TransactionService.class);
    }

    @Override
    protected void doGetInternal(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws Exception {
        PrintWriter writer = resp.getWriter();
        DateRange range = parseDateRange(req, resp);
        LocalDate startDate = range.getStartDate();
        LocalDate endDate = range.getEndDate();

        List<CategoryAmountModel> transactions = transactionService.getIncomeReportByCategory(userId, startDate, endDate);
        resp.setStatus(HttpServletResponse.SC_OK);
        writer.write(transactions.toString());
    }
}
