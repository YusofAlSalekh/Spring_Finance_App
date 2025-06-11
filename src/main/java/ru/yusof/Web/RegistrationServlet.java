package ru.yusof.Web;

import ru.yusof.exceptions.AlreadyExistsException;
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
        if (login == null || password == null || login.isBlank() || password.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Login and password must be provided");
            return;
        }

        try {
            UserDTO user = authService.register(login, password);
            writer.write(user.toString());
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (AlreadyExistsException e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "User already exists");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error during registration");
        }
    }
}
