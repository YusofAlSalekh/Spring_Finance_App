package com.yusof.web.api.controller;

import com.yusof.web.api.json.request.AuthorizationRequest;
import com.yusof.web.api.json.request.RegistrationRequest;
import com.yusof.web.api.json.response.AuthenticationResponse;
import com.yusof.web.service.AuthenticationService;
import com.yusof.web.service.ClientDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client")
public class AuthenticationController extends AbstractApiController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegistrationRequest request) {
        ClientDTO clientDTO = authenticationService.register(
                request.getLogin(),
                request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthenticationResponse.fromDTO(clientDTO));
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody @Valid AuthorizationRequest request, HttpServletRequest httpServletRequest) {
        ClientDTO clientDTO = authenticationService.authorize(
                request.getEmail(),
                request.getPassword());

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("clientId", clientDTO.getId());

        return AuthenticationResponse.fromDTO(clientDTO);
    }

    @GetMapping
    public AuthenticationResponse getClient(HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        ClientDTO clientDTO = authenticationService.getClientById(clientId);
        return AuthenticationResponse.fromDTO(clientDTO);
    }
}
