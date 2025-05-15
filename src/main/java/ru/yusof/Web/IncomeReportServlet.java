package ru.yusof.Web;

import ru.yusof.dao.CategoryAmountModel;
import ru.yusof.service.TransactionService;
import ru.yusof.view.SpringContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class IncomeReportServlet extends HttpServlet {
    private final TransactionService transactionService;

    public IncomeReportServlet() {
        this.transactionService = SpringContext.getContext().getBean(TransactionService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        LocalDate startDate = LocalDate.parse(req.getParameter("start"));
        LocalDate endDate = LocalDate.parse(req.getParameter("end"));

        if (userId == null) {
            writer.write("Access denied");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            List<CategoryAmountModel> transactions = transactionService.getIncomeReportByCategory(userId, startDate, endDate);
            writer.write(transactions.toString());
        }
    }
}
