package ru.yusof.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.request.AuthorizationRequest;
import ru.yusof.json.response.AuthorizationResponse;
import ru.yusof.service.AuthorizationService;
import ru.yusof.service.UserDTO;

@Service("/login")
@RequiredArgsConstructor
public class AuthorisationController implements Controller<AuthorizationRequest, AuthorizationResponse> {
    private final AuthorizationService authorizationService;

    @Override
    public AuthorizationResponse handle(AuthorizationRequest request) {
        UserDTO userDTO = authorizationService.authorize(request.getEmail(), request.getPassword());
        return new AuthorizationResponse(userDTO.getId(), userDTO.getEmail());
    }

    @Override
    public Class<AuthorizationRequest> getRequestClass() {
        return AuthorizationRequest.class;
    }
}
