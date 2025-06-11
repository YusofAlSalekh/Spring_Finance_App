package ru.yusof.Web;

import ru.yusof.service.AuthorizationService;
import ru.yusof.service.UserDTO;
import ru.yusof.view.SpringContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class UserServlet extends BaseServlet {
    private final AuthorizationService authService;

    public UserServlet() {
        this.authService = SpringContext.getContext().getBean(AuthorizationService.class);
    }

    @Override
    protected void doGetInternal(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws Exception {
        PrintWriter writer = resp.getWriter();
        UserDTO user = authService.getUserById(userId);
        writer.write(user.toString());
    }
}
