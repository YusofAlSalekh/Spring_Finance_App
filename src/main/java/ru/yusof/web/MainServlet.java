package ru.yusof.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.ApplicationContext;
import ru.yusof.controller.AuthorisationController;
import ru.yusof.controller.Controller;
import ru.yusof.controller.SecureController;
import ru.yusof.exceptions.*;
import ru.yusof.json.request.AuthorizationRequest;
import ru.yusof.json.response.AuthorizationResponse;
import ru.yusof.json.response.ErrorResponse;
import ru.yusof.view.SpringContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainServlet extends HttpServlet {
    private Map<String, Controller> controllers = new HashMap<>();
    private Map<String, SecureController> secureControllers = new HashMap<>();
    private ObjectMapper om = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public MainServlet() {
        ApplicationContext context = SpringContext.getContext();

        for (String beanName : context.getBeanNamesForType(Controller.class)) {
            controllers.put(beanName, context.getBean(beanName, Controller.class));
        }

        for (String beanName : context.getBeanNamesForType(SecureController.class)) {
            secureControllers.put(beanName, context.getBean(beanName, SecureController.class));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        handleRequest(req, res);
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {

        try {
            dispatchToController(req, res);
        } catch (UnauthorizedException e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            om.writeValue(res.getWriter(), new ErrorResponse(e.getMessage()));
        } catch (AlreadyExistsException e) {
            res.setStatus(HttpServletResponse.SC_CONFLICT);
            om.writeValue(res.getWriter(), new ErrorResponse(e.getMessage()));
        } catch (NotFoundException e) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            om.writeValue(res.getWriter(), new ErrorResponse(e.getMessage()));
        } catch (OperationFailedException e) {
            res.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            om.writeValue(res.getWriter(), new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            om.writeValue(res.getWriter(), new ErrorResponse(e.getMessage()));
        } catch (ForbiddenException e) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            om.writeValue(res.getWriter(), new ErrorResponse(e.getMessage()));
        } catch (InvalidFormatException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            om.writeValue(res.getWriter(), new ErrorResponse("Invalid data type"));
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            om.writeValue(res.getWriter(), new ErrorResponse(e.getMessage()));
        }
    }

    private void dispatchToController(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String uri = req.getRequestURI();
        Controller controller = controllers.get(uri);

        if (controller != null) {
            handleController(req, res, controller);
            return;
        }
        SecureController secureController = secureControllers.get(uri);

        if (secureController != null) {
            handleSecureController(req, res, secureController);
            return;
        }
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    private void handleSecureController(HttpServletRequest req, HttpServletResponse res, SecureController secureController) throws IOException {
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            om.writeValue(res.getWriter(), secureController.handle(
                            om.readValue(req.getInputStream(), secureController.getRequestClass()),
                            userId
                    )
            );
        }
    }

    private void handleController(HttpServletRequest req, HttpServletResponse res, Controller controller) throws IOException {
        if (controller instanceof AuthorisationController) {
            handleAuthController(req, res, (AuthorisationController) controller);
        } else {
            om.writeValue(res.getWriter(), controller.handle(
                            om.readValue(req.getInputStream(), controller.getRequestClass())
                    )
            );
        }
    }

    private void handleAuthController(HttpServletRequest req, HttpServletResponse res, AuthorisationController controller) throws IOException {
        AuthorizationRequest authorizationRequest = om.readValue(req.getInputStream(), controller.getRequestClass());
        AuthorizationResponse authorizationResponse = controller.handle(authorizationRequest);

        if (authorizationResponse == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            HttpSession session = req.getSession();
            session.setAttribute("userId", authorizationResponse.getId());
            om.writeValue(res.getWriter(), authorizationResponse);
        }
    }
}
