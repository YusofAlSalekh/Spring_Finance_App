package ru.yusof.Web;

import ru.yusof.exceptions.BadCredentialsException;
import ru.yusof.service.AuthorizationService;
import ru.yusof.service.UserDTO;
import ru.yusof.view.SpringContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthorisationServlet extends HttpServlet {
    private final AuthorizationService authService;

    public AuthorisationServlet() {
        this.authService = SpringContext.getContext().getBean(AuthorizationService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        PrintWriter writer = resp.getWriter();
        UserDTO user;
        try {
            user = authService.authorize(login, password);
        } catch (BadCredentialsException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return;
        }

        if (user == null) {
            writer.write("Access denied, incorrect login or password");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            writer.write(user.toString());
            HttpSession session = req.getSession();
            session.setAttribute("userId", user.getId());
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
