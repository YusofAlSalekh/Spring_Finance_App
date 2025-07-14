package ru.yusof.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.AuthRequest;
import ru.yusof.json.AuthResponse;
import ru.yusof.service.AuthorizationService;
import ru.yusof.service.UserDTO;

@Service("/login")
@RequiredArgsConstructor
public class AuthorisationController implements Controller<AuthRequest, AuthResponse> {
    private final AuthorizationService authorizationService;

    @Override
    public AuthResponse handle(AuthRequest request) {
        UserDTO userDTO = authorizationService.authorize(request.getEmail(), request.getPassword());
        return new AuthResponse(userDTO.getId(), userDTO.getEmail());
    }

    @Override
    public Class<AuthRequest> getRequestClass() {
        return AuthRequest.class;
    }
}
