package com.yusof.web.web.controller;

import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.service.AuthenticationService;
import com.yusof.web.service.ClientDTO;
import com.yusof.web.web.form.RegistrationForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebAuthenticationController.class)
@Import(MockSecurityConfig.class)
class WebAuthenticationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AuthenticationService authenticationService;

    @Test
    void getLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void getRegistration() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("form"))
                .andExpect(model().attribute("form", instanceOf(RegistrationForm.class)));
    }

    @Test
    void postRegistration_success() throws Exception {
        when(authenticationService.register("user@mail.ru", "password"))
                .thenReturn(new ClientDTO(1, "user@mail.ru"));

        mockMvc.perform(post("/register")
                        .param("login", "user@mail.ru")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    void postRegistration_Exception_Thrown_userExists() throws Exception {
        when(authenticationService.register("user@mail.ru", "password"))
                .thenThrow(new AlreadyExistsException("User already exists!"));

        mockMvc.perform(post("/register")
                        .param("login", "user@mail.ru")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeHasFieldErrors("form", "login"));
    }

    @Test
    void postRegistration_BlankCredentials() throws Exception {
        mockMvc.perform(post("/register")
                        .param("login", "")
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeHasErrors("form"));
    }
}