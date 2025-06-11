package ru.yusof.Web;

import ru.yusof.exceptions.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

public abstract class BaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            HttpSession session = req.getSession(false);

            if (session == null) {
                throw new UnauthorizedException("No session found");
            } else {
                Integer userId = (Integer) session.getAttribute("userId");
                if (userId == null) {
                    throw new UnauthorizedException("User not logged in");
                }
                doGetInternal(req, resp, userId);
            }
        } catch (UnauthorizedException e) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (AlreadyExistsException e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (NoSuchElementException | DeletionAccountException | UserNotFoundByIdException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (CreatingAccountException | CreatingTransactionCategoryException |
                 DeletionTransactionCategoryException | AddingTransactionCategoryException |
                 AddTransactionException e) {
            resp.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
        } catch (IllegalOwnerException | AddingTheAmountException | SubtractingTheAmountException |
                 InsufficientFundsException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong");
        }
    }

    protected abstract void doGetInternal(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws Exception;

    protected LocalDate[] parseDateRange(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            LocalDate startDate = LocalDate.parse(req.getParameter("start"));
            LocalDate endDate = LocalDate.parse(req.getParameter("end"));
            return new LocalDate[]{startDate, endDate};
        } catch (DateTimeParseException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format. Expected format is yyyy-MM-dd.");
            return null;
        }
    }
}
