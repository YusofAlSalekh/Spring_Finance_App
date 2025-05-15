package ru.yusof.Web;

import ru.yusof.service.TransactionCategoryService;
import ru.yusof.view.SpringContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class TransCatDeletionServlet extends HttpServlet {
    private final TransactionCategoryService transactionCategoryService;

    public TransCatDeletionServlet() {
        this.transactionCategoryService = SpringContext.getContext().getBean(TransactionCategoryService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        int transactionCategoryId = Integer.parseInt(req.getParameter("id"));

        if (userId == null) {
            writer.write("Access denied");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            boolean deleted = transactionCategoryService.deleteTransactionCategory(transactionCategoryId, userId);
            writer.write(String.valueOf(deleted));
        }
    }
}
