package ru.yusof.Web;

import ru.yusof.exceptions.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
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
        } catch (NotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (OperationFailedException e) {
            resp.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (ForbiddenException e) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong");
        }
    }

    protected abstract void doGetInternal(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws Exception;

    protected DateRange parseDateRange(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            LocalDate startDate = LocalDate.parse(req.getParameter("start"));
            LocalDate endDate = LocalDate.parse(req.getParameter("end"));
            return new DateRange(startDate, endDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format is yyyy-MM-dd.");
        }
    }

    protected BigDecimal extractPositiveBigDecimal(HttpServletRequest request, String paramName) {
        try {
            BigDecimal result = new BigDecimal(request.getParameter(paramName));
            if (result.signum() < 0) {
                throw new IllegalArgumentException(paramName + " should be a positive number");
            }
            return result;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(paramName + " should be a valid number");
        }
    }

    protected Integer extractPositiveInteger(HttpServletRequest request, String paramName) {
        return parsePositiveInteger(request.getParameter(paramName), paramName);
    }

    protected String extractNotBlankString(HttpServletRequest request, String paramName) {
        String result = request.getParameter(paramName);
        if (result == null || result.isBlank()) {
            throw new IllegalArgumentException(paramName + " is required");
        }
        return result;
    }

    protected String[] extractNotEmptyArray(HttpServletRequest request, String paramName) {
        String[] result = request.getParameterValues(paramName);
        if (result == null || result.length == 0) {
            throw new IllegalArgumentException(paramName + " is required");
        }
        return result;
    }

    protected List<Integer> extractIntegerList(HttpServletRequest request, String paramName) {
        String[] values = extractNotEmptyArray(request, paramName);
        List<Integer> result = new ArrayList<>();
        for (String value : values) {
            result.add(parsePositiveInteger(value, value));
        }
        return result;
    }

    private Integer parsePositiveInteger(String rawParam, String paramName) {
        try {
            int result = Integer.parseInt(rawParam);
            if (Integer.signum(result) < 0) {
                throw new IllegalArgumentException(paramName + " should be a positive number");
            }
            return result;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(paramName + " should be a valid number");
        }
    }
}

