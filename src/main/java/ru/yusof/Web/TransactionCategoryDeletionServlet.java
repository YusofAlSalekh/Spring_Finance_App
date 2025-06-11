package ru.yusof.Web;

import ru.yusof.service.TransactionCategoryService;
import ru.yusof.view.SpringContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TransactionCategoryDeletionServlet extends BaseServlet {
    private final TransactionCategoryService transactionCategoryService;

    public TransactionCategoryDeletionServlet() {
        this.transactionCategoryService = SpringContext.getContext().getBean(TransactionCategoryService.class);
    }

    @Override
    protected void doGetInternal(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws Exception {
        String idParam = req.getParameter("id");

        int transactionCategoryId;
        try {
            transactionCategoryId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "id should be a valid number");
            return;
        }
        transactionCategoryService.deleteTransactionCategory(transactionCategoryId, userId);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
