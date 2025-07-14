package ru.yusof.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.GetUserRequest;
import ru.yusof.json.GetUserResponse;
import ru.yusof.service.AuthorizationService;
import ru.yusof.service.UserDTO;

@AllArgsConstructor
@Service("/user")
public class GetUserController implements SecureController<GetUserRequest, GetUserResponse> {
    private final AuthorizationService authorizationService;

    @Override
    public GetUserResponse handle(GetUserRequest request, Integer userId) {
        UserDTO user = authorizationService.getUserById(userId);
        return new GetUserResponse(user);
    }

    @Override
    public Class<GetUserRequest> getRequestClass() {
        return GetUserRequest.class;
    }
}
