package ru.yusof.Web;

import ru.yusof.service.AccountService;
import ru.yusof.view.SpringContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccountDeletionServlet extends BaseServlet {
    private final AccountService accountService;

    public AccountDeletionServlet() {
        this.accountService = SpringContext.getContext().getBean(AccountService.class);
    }

    @Override
    protected void doGetInternal(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws Exception {
        int accountId = extractPositiveInteger(req, "accountId");
        accountService.deleteAccount(accountId, userId);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
