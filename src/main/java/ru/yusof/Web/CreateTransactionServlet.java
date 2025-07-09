package ru.yusof.Web;

import ru.yusof.service.TransactionService;
import ru.yusof.view.SpringContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

public class CreateTransactionServlet extends BaseServlet {
    private final TransactionService transactionService;

    public CreateTransactionServlet() {
        this.transactionService = SpringContext.getContext().getBean(TransactionService.class);
    }

    @Override
    protected void doGetInternal(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws Exception {
        PrintWriter writer = resp.getWriter();
        BigDecimal amount = extractPositiveBigDecimal(req, "amount");
        int senderAccountId = extractPositiveInteger(req, "sender_account_id");
        int receiverAccountId = extractPositiveInteger(req, "receiver_account_id");
        List<Integer> categoryIds = extractIntegerList(req, "category_ids");

        transactionService.performTransaction(senderAccountId, receiverAccountId, userId, amount, categoryIds);
        resp.setStatus(HttpServletResponse.SC_OK);
        writer.write("Transaction created successfully");
    }
}
