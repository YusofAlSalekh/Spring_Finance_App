package ru.yusof.Web;

import ru.yusof.service.AccountDTO;
import ru.yusof.service.AccountService;
import ru.yusof.view.SpringContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

public class AccountsViewingServlet extends BaseServlet {
    private final AccountService accountService;

    public AccountsViewingServlet() {
        this.accountService = SpringContext.getContext().getBean(AccountService.class);
    }

    @Override
    protected void doGetInternal(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws Exception {
        PrintWriter writer = resp.getWriter();
        List<AccountDTO> listOfAccounts = accountService.viewAccount(userId);
        resp.setStatus(HttpServletResponse.SC_OK);
        writer.write(listOfAccounts.toString());
    }
}
