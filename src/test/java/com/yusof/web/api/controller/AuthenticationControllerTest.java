package com.yusof.web.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yusof.web.api.json.request.RegistrationRequest;
import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.service.AuthenticationService;
import com.yusof.web.service.ClientDTO;
import com.yusof.web.web.controller.MockSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(MockSecurityConfig.class)
@WebMvcTest(AuthenticationController.class)
@WithUserDetails(
        value = "user@gmail.com",
        userDetailsServiceBeanName = "userDetailsService"
)
class AuthenticationControllerTest {
    @MockitoBean
    AuthenticationService authenticationService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getClient_returnsUserDto() throws Exception {
        ClientDTO dto = new ClientDTO(1, "user@gmail.com");

        when(authenticationService.getClientById(1))
                .thenReturn(dto);

        mockMvc.perform(get("/api/client"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));

        verify(authenticationService).getClientById(1);
    }

    @Test
    void getClient_notFound_returns404() throws Exception {
        when(authenticationService.getClientById(1))
                .thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/api/client"))
                .andExpect(status().isNotFound());
    }

    @WithAnonymousUser
    @Test
    void register_success() throws Exception {
        ClientDTO dto = new ClientDTO(1, "user@mail.com");
        RegistrationRequest request = new RegistrationRequest("user@mail.com", "password");

        when(authenticationService.register("user@mail.com", "password"))
                .thenReturn(dto);

        mockMvc.perform(post("/api/client/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user@mail.com"));

        verify(authenticationService).register("user@mail.com", "password");
    }

    @WithAnonymousUser
    @Test
    void register_exception_clientExists() throws Exception {
        RegistrationRequest request = new RegistrationRequest("user@mail.com", "password");

        when(authenticationService.register("user@mail.com", "password"))
                .thenThrow(new AlreadyExistsException("Email already in use"));

        mockMvc.perform(post("/api/client/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @WithAnonymousUser
    @Test
    void register_validationError_returns400() throws Exception {
        RegistrationRequest request = new RegistrationRequest("", "password");

        mockMvc.perform(post("/api/client/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}