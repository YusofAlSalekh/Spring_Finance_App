package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.RegistrationRequest;
import com.yusof.web.api.json.response.RegistrationResponse;
import com.yusof.web.service.AuthorizationService;
import com.yusof.web.service.ClientDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RegistrationController {
    private final AuthorizationService authorizationService;

    @PostMapping("/register")
    public RegistrationResponse register(@RequestBody @Valid RegistrationRequest request) {
        ClientDTO clientDTO = authorizationService.register(
                request.getLogin(),
                request.getPassword());
        return new RegistrationResponse(clientDTO.getId(), clientDTO.getEmail());
    }
}