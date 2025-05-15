package ru.yusof.Web;

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

public class AuthServlet extends HttpServlet {
    private final AuthorizationService authService;

    public AuthServlet() {
        this.authService = SpringContext.getContext().getBean(AuthorizationService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        PrintWriter writer = resp.getWriter();
        UserDTO user = authService.authorize(login, password);

        if (user == null) {
            writer.write("Access denied");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            writer.write(user.toString());
            HttpSession session = req.getSession();
            session.setAttribute("userId", user.getId());
        }
    }
}
