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
        String accountName = extractNotBlankString(req, "accountName");
        int accountId = extractPositiveInteger(req, "accountId");

        AccountDTO updatedAccountName = accountService.updateAccountName(accountName, accountId, userId);
        resp.setStatus(HttpServletResponse.SC_OK);
        writer.write(updatedAccountName.toString());
    }
}
