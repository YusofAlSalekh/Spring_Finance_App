package ru.yusof.Web;

import ru.yusof.service.AccountDTO;
import ru.yusof.service.AccountService;
import ru.yusof.view.SpringContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class AccountUpdatingNameServlet extends BaseServlet {
    private final AccountService accountService;

    public AccountUpdatingNameServlet() {
        this.accountService = SpringContext.getContext().getBean(AccountService.class);
    }

    @Override
    protected void doGetInternal(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws Exception {
        PrintWriter writer = resp.getWriter();
        String accountName = req.getParameter("accountName");
        String accountIdParam = req.getParameter("accountId");

        if (accountName == null || accountIdParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Both accountName and accountId are required");
            return;
        }

        int accountId;
        try {
            accountId = Integer.parseInt(req.getParameter("accountId"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "accountId should be a valid number");
            return;
        }
        AccountDTO updatedAccountName = accountService.updateAccountName(accountName, accountId, userId);
        resp.setStatus(HttpServletResponse.SC_OK);
        writer.write(updatedAccountName.toString());
    }
}
