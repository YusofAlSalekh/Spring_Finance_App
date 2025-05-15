package ru.yusof.Web;

import ru.yusof.service.AccountDTO;
import ru.yusof.service.AccountService;
import ru.yusof.view.SpringContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class AccountCreationServlet extends HttpServlet {
    private final AccountService accountService;

    public AccountCreationServlet() {
        this.accountService = SpringContext.getContext().getBean(AccountService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        String accountName = req.getParameter("accountName");
        BigDecimal balance = new BigDecimal(req.getParameter("balance"));

        if (userId == null) {
            writer.write("Access denied");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            AccountDTO account = accountService.createAccount(accountName, balance, userId);
            writer.write(account.toString());
        }
    }
}
