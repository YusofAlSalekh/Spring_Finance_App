package ru.yusof.Web;

import ru.yusof.service.AccountDTO;
import ru.yusof.service.AccountService;
import ru.yusof.view.SpringContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class AccountCreationServlet extends BaseServlet {
    private final AccountService accountService;

    public AccountCreationServlet() {
        this.accountService = SpringContext.getContext().getBean(AccountService.class);
    }

    @Override
    protected void doGetInternal(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws Exception {
        PrintWriter writer = resp.getWriter();
        String accountName = req.getParameter("name");
        String balanceParam = req.getParameter("balance");

        if (accountName == null || accountName.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account name is required");
            return;
        }

        BigDecimal balance;
        try {
            balance = new BigDecimal(balanceParam);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid balance value");
            return;
        }
        AccountDTO account = accountService.createAccount(accountName, balance, userId);
        resp.setStatus(HttpServletResponse.SC_OK);
        writer.write(account.toString());
    }
}
