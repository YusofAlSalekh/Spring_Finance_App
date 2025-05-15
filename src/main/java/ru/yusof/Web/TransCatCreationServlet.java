package ru.yusof.Web;

import ru.yusof.service.TransactionCategoryDTO;
import ru.yusof.service.TransactionCategoryService;
import ru.yusof.view.SpringContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class TransCatCreationServlet extends HttpServlet {
    private final TransactionCategoryService transactionCategoryService;

    public TransCatCreationServlet() {
        this.transactionCategoryService = SpringContext.getContext().getBean(TransactionCategoryService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        String categoryName = req.getParameter("name");

        if (userId == null) {
            writer.write("Access denied");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            TransactionCategoryDTO transactionCategory = transactionCategoryService.createCategory(categoryName, userId);
            writer.write(transactionCategory.toString());
        }
    }
}
