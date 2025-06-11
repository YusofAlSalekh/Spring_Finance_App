package ru.yusof.Web;

import ru.yusof.service.TransactionService;
import ru.yusof.view.SpringContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreateTransactionServlet extends BaseServlet {
    private final TransactionService transactionService;

    public CreateTransactionServlet() {
        this.transactionService = SpringContext.getContext().getBean(TransactionService.class);
    }

    @Override
    protected void doGetInternal(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws Exception {
        PrintWriter writer = resp.getWriter();
        BigDecimal amount;
        int senderAccountId;
        int receiverAccountId;
        List<Integer> categoryIds = new ArrayList<>();
        try {
            amount = new BigDecimal(req.getParameter("amount"));
            senderAccountId = Integer.parseInt(req.getParameter("sender_account_id"));
            receiverAccountId = Integer.parseInt(req.getParameter("receiver_account_id"));

            String[] categories = req.getParameterValues("categoryIds");
            if (categories == null || categories.length == 0) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "At least one category ID is required");
                return;
            }

            for (String category : categories) {
                categoryIds.add(Integer.parseInt(category));
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "amount, senderAccountId or receiverAccountId are not valid numbers");
            return;
        }
        transactionService.performTransaction(senderAccountId, receiverAccountId, userId, amount, categoryIds);
        resp.setStatus(HttpServletResponse.SC_OK);
        writer.write("Transaction created successfully");
    }
}
