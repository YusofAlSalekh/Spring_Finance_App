package com.yusof.web.api.controller;

import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.service.AuthenticationService;
import com.yusof.web.service.ClientDTO;
import com.yusof.web.web.controller.MockSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
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
class AuthenticationControllerTest {
    @MockitoBean
    AuthenticationService authenticationService;

    @Autowired
    MockMvc mockMvc;

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void getClient_returnsUserDto() throws Exception {
        when(authenticationService.getClientById(1))
                .thenReturn(new ClientDTO(1, "user@gmail.com"));

        mockMvc.perform(get("/api/client"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "email": "user@gmail.com"
                        }"""));

        verify(authenticationService).getClientById(1);
    }

    @WithUserDetails(
            value = "notUser@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void getClient_notFound_returns404() throws Exception {
        when(authenticationService.getClientById(1))
                .thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/api/client"))
                .andExpect(status().isNotFound());
    }

    @Test
    void register_success() throws Exception {
        when(authenticationService.register("user@mail.com", "password"))
                .thenReturn(new ClientDTO(1, "user@mail.com"));

        String body = """
                  { "login": "user@mail.com",
                   "password": "password" }
                """;

        mockMvc.perform(post("/api/client/register")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user@mail.com"));

        verify(authenticationService).register("user@mail.com", "password");
    }

    @Test
    void register_Exception_clientExists() throws Exception {
        when(authenticationService.register("user@mail.com", "password"))
                .thenThrow(new AlreadyExistsException("Email already in use"));

        String body = """
                  {"login":"user@mail.com","password":"password"}
                """;

        mockMvc.perform(post("/api/client/register")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void register_validationError_returns400() throws Exception {
        String body = """
                {"login":"","password":""}
                """;

        mockMvc.perform(post("/api/client/register")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}