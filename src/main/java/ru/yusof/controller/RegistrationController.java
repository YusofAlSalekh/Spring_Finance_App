package ru.yusof.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.RegistrationRequest;
import ru.yusof.json.RegistrationResponse;
import ru.yusof.service.AuthorizationService;
import ru.yusof.service.UserDTO;

@Service("/register")
@RequiredArgsConstructor
public class RegistrationController implements Controller<RegistrationRequest, RegistrationResponse> {
    private final AuthorizationService authorizationService;

    @Override
    public RegistrationResponse handle(RegistrationRequest request) {
        if (request.getLogin() == null || request.getPassword() == null || request.getLogin().isBlank() || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Login and password must be provided");
        }

        UserDTO userDTO = authorizationService.register(request.getLogin(), request.getPassword());
        return new RegistrationResponse(userDTO.getId(), userDTO.getEmail());
    }

    @Override
    public Class<RegistrationRequest> getRequestClass() {
        return RegistrationRequest.class;
    }
}
