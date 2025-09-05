package com.yusof.web.api.controller;

import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.service.AccountDTO;
import com.yusof.web.service.AccountService;
import com.yusof.web.web.controller.MockSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@Import(MockSecurityConfig.class)
class AccountControllerTest {

    @MockitoBean
    AccountService accountService;

    @Autowired
    MockMvc mockMvc;

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void createAccount_success() throws Exception {
        AccountDTO accountDTO = new AccountDTO(1, "AccountName", new BigDecimal("100.00"), 1);

        when(accountService.createAccount("AccountName", new BigDecimal("100.00"), 1))
                .thenReturn(accountDTO);

        mockMvc.perform(post("/api/account/create")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"AccountName\",\"balance\":100.00}"))
                .andExpect(status().isCreated());

        verify(accountService).createAccount("AccountName", new BigDecimal("100.00"), 1);
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void createAccount_AlreadyExistsException() throws Exception {
        when(accountService.createAccount("AccountName", new BigDecimal("10.00"), 1))
                .thenThrow(new AlreadyExistsException("Account already exists"));

        String body = """
                  {"name":"AccountName","balance": 10.00}
                """;

        mockMvc.perform(post("/api/account/create")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void createAccount_validationError() throws Exception {
        String body = """
                  {"name":"","balance":-5}
                """;

        mockMvc.perform(post("/api/account/create")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void deleteAccount_success() throws Exception {
        String body = """
                  {"accountId": 5}
                """;

        mockMvc.perform(post("/api/account/delete")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent());

        verify(accountService).deleteAccount(5, 1);
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void deleteAccount_notFound_404() throws Exception {
        doThrow(new NotFoundException("No account found with Id: 5"))
                .when(accountService).deleteAccount(5, 1);

        String body = """
                  {"accountId": 5}
                """;

        mockMvc.perform(post("/api/account/delete")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void deleteAccount_validationError_400() throws Exception {
        String body = """
                  {"accountId": 0}
                """;

        mockMvc.perform(post("/api/account/delete")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void updateAccount_success() throws Exception {
        when(accountService.updateAccountName("NewName", 5, 1))
                .thenReturn(new AccountDTO(5, "NewName", new BigDecimal("10.00"), 1));

        String body = """
                  {"name":"NewName","accountId":5}
                """;

        mockMvc.perform(post("/api/account/update")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewName"));

        verify(accountService).updateAccountName("NewName", 5, 1);
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void updateAccount_AlreadyExists() throws Exception {
        when(accountService.updateAccountName("Existing", 5, 1))
                .thenThrow(new AlreadyExistsException("The account with this name already exists"));

        String body = """
                  {"name":"Existing","accountId":5}
                """;

        mockMvc.perform(post("/api/account/update")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void updateAccount_notFound_404() throws Exception {
        when(accountService.updateAccountName("NewName", 99, 1))
                .thenThrow(new NotFoundException("No account found with Id: 99"));

        String body = """
                  {"name":"NewName","accountId":99}
                """;

        mockMvc.perform(post("/api/account/update")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void updateAccount_validationError_400() throws Exception {
        String body = """
                  {"name":"","accountId":0}
                """;

        mockMvc.perform(post("/api/account/update")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void showAccounts_ok_returnsList() throws Exception {
        when(accountService.viewAccount(1)).thenReturn(List.of(
                new AccountDTO(1, "Cash", new BigDecimal("100.00"), 1),
                new AccountDTO(2, "Card", new BigDecimal("50.50"), 1)
        ));

        mockMvc.perform(get("/api/account/show")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Cash"))
                .andExpect(jsonPath("$[1].name").value("Card"));

        verify(accountService).viewAccount(1);
    }
}