package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.AuthorizationRequest;
import com.yusof.web.api.json.response.AuthorizationResponse;
import com.yusof.web.service.AuthorizationService;
import com.yusof.web.service.ClientDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthorisationController {
    private final AuthorizationService authorizationService;

    @PostMapping("/login")
    public AuthorizationResponse login(@RequestBody @Valid AuthorizationRequest request, HttpServletRequest httpServletRequest) {
        ClientDTO clientDTO = authorizationService.authorize(
                request.getEmail(),
                request.getPassword());

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("clientId", clientDTO.getId());

        return new AuthorizationResponse(clientDTO.getId(), clientDTO.getEmail());
    }
}
