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
        String newNameParam = extractNotBlankString(req, "name");
        int transactionCategoryId = extractPositiveInteger(req, "id");

        boolean updatedTransCat = transactionCategoryService.editTransactionCategory(newNameParam, transactionCategoryId, userId);
        writer.write(String.valueOf(updatedTransCat));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
