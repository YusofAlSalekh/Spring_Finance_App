package ru.yusof.Web;

import ru.yusof.service.AuthorizationService;
import ru.yusof.service.UserDTO;
import ru.yusof.view.SpringContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class RegistrationServlet extends HttpServlet {
    private final AuthorizationService authService;

    public RegistrationServlet() {
        this.authService = SpringContext.getContext().getBean(AuthorizationService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Writer writer = resp.getWriter();
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        UserDTO user = authService.register(login, password);

        if (user == null) {
            writer.write("Problem during registration");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            writer.write(user.toString());
        }
    }
}
