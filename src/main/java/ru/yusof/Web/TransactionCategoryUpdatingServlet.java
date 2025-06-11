package ru.yusof.Web;

import ru.yusof.service.TransactionCategoryService;
import ru.yusof.view.SpringContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class TransactionCategoryUpdatingServlet extends BaseServlet {
    private final TransactionCategoryService transactionCategoryService;

    public TransactionCategoryUpdatingServlet() {
        this.transactionCategoryService = SpringContext.getContext().getBean(TransactionCategoryService.class);
    }

    @Override
    protected void doGetInternal(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws Exception {
        PrintWriter writer = resp.getWriter();

        String newNameParam = req.getParameter("name");
        if (newNameParam == null || newNameParam.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Name is required");
            return;
        }

        int transactionCategoryId;
        try {
            transactionCategoryId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "transaction category id must be a valid number");
            return;
        }

        boolean updatedTransCat = transactionCategoryService.editTransactionCategory(newNameParam, transactionCategoryId, userId);
        writer.write(String.valueOf(updatedTransCat));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
