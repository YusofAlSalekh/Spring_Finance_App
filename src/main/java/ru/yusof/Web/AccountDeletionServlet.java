package ru.yusof.Web;

import ru.yusof.service.AccountService;
import ru.yusof.view.SpringContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class AccountDeletionServlet extends HttpServlet {
    private final AccountService accountService;

    public AccountDeletionServlet() {
        this.accountService = SpringContext.getContext().getBean(AccountService.class);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        int accountId = Integer.parseInt(req.getParameter("accountId"));

        if (userId == null) {
            writer.write("Access denied");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            accountService.deleteAccount(accountId, userId);
        }
    }
}
