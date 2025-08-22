package com.yusof.web.api.controller;

import com.yusof.web.api.json.response.GetClientResponse;
import com.yusof.web.service.AuthorizationService;
import com.yusof.web.service.ClientDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.yusof.web.api.controller.SessionUtils.getClientId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GetClientController {
    private final AuthorizationService authorizationService;

    @GetMapping("/client")
    public GetClientResponse getClient(HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        ClientDTO clientDTO = authorizationService.getClientById(clientId);
        return new GetClientResponse(clientDTO);
    }
}
