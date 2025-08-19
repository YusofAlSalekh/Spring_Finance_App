package com.yusof.web.api.controller;


import com.yusof.web.api.json.request.GetClientRequest;
import com.yusof.web.api.json.response.GetClientResponse;
import com.yusof.web.service.AuthorizationService;
import com.yusof.web.service.ClientDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.yusof.web.api.controller.SessionUtils.getClientId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GetClientController {
    private final AuthorizationService authorizationService;

    @PostMapping("/client")
    public GetClientResponse getClient(@RequestBody GetClientRequest request, HttpServletRequest httpServletRequest) {
        Integer clientId = getClientId(httpServletRequest);

        ClientDTO clientDTO = authorizationService.getClientById(clientId);
        return new GetClientResponse(clientDTO);
    }
}
