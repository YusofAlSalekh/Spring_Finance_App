package com.yusof.web.api.controller;

import com.yusof.web.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {
    public static Integer getClientId(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("clientId") == null) {
            throw new UnauthorizedException("User is not logged in");
        }

        return (Integer) session.getAttribute("clientId");
    }
}
