package ru.yusof.Web;

import ru.yusof.service.TransactionCategoryDTO;
import ru.yusof.service.TransactionCategoryService;
import ru.yusof.view.SpringContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class TransactionCategoryCreationServlet extends BaseServlet {
    private final TransactionCategoryService transactionCategoryService;

    public TransactionCategoryCreationServlet() {
        this.transactionCategoryService = SpringContext.getContext().getBean(TransactionCategoryService.class);
    }

    @Override
    protected void doGetInternal(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws Exception {
        PrintWriter writer = resp.getWriter();
        String categoryName = req.getParameter("name");

        if (categoryName == null || categoryName.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Category name is required");
            return;
        }
        TransactionCategoryDTO transactionCategory = transactionCategoryService.createCategory(categoryName, userId);
        writer.write(transactionCategory.toString());
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
