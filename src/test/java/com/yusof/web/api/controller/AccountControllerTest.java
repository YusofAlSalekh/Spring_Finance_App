package com.yusof.web.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yusof.web.api.json.request.AccountCreationRequest;
import com.yusof.web.api.json.request.AccountDeletionRequest;
import com.yusof.web.api.json.request.AccountNameChangingRequest;
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
@WithUserDetails(
        value = "user@gmail.com",
        userDetailsServiceBeanName = "userDetailsService"
)
class AccountControllerTest {

    @MockitoBean
    AccountService accountService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createAccount_success() throws Exception {
        AccountDTO accountDTO = new AccountDTO(1, "AccountName", new BigDecimal("100.00"), 1);

        when(accountService.createAccount("AccountName", new BigDecimal("100.00"), 1))
                .thenReturn(accountDTO);

        AccountCreationRequest request = new AccountCreationRequest("AccountName", new BigDecimal("100.00"));

        mockMvc.perform(post("/api/account/create")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(accountService).createAccount("AccountName", new BigDecimal("100.00"), 1);
    }

    @Test
    void createAccount_AlreadyExistsException() throws Exception {
        when(accountService.createAccount("AccountName", new BigDecimal("10.00"), 1))
                .thenThrow(new AlreadyExistsException("Account already exists"));

        AccountCreationRequest request = new AccountCreationRequest("AccountName", new BigDecimal("10.00"));

        mockMvc.perform(post("/api/account/create")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void createAccount_validationError() throws Exception {
        AccountCreationRequest request = new AccountCreationRequest("", new BigDecimal("-5.00"));

        mockMvc.perform(post("/api/account/create")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteAccount_success() throws Exception {
        AccountDeletionRequest request = new AccountDeletionRequest(5);

        mockMvc.perform(post("/api/account/delete")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(accountService).deleteAccount(5, 1);
    }

    @Test
    void deleteAccount_notFound_404() throws Exception {
        AccountDeletionRequest request = new AccountDeletionRequest(5);

        doThrow(new NotFoundException("No account found with Id: 5"))
                .when(accountService).deleteAccount(5, 1);

        mockMvc.perform(post("/api/account/delete")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAccount_validationError_400() throws Exception {
        AccountDeletionRequest request = new AccountDeletionRequest(0);

        mockMvc.perform(post("/api/account/delete")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAccount_success() throws Exception {
        AccountNameChangingRequest request = new AccountNameChangingRequest("NewName", 5);

        when(accountService.updateAccountName("NewName", 5, 1))
                .thenReturn(new AccountDTO(5, "NewName", new BigDecimal("10.00"), 1));

        mockMvc.perform(post("/api/account/update")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewName"));

        verify(accountService).updateAccountName("NewName", 5, 1);
    }

    @Test
    void updateAccount_AlreadyExists() throws Exception {
        AccountNameChangingRequest request = new AccountNameChangingRequest("Existing", 5);

        when(accountService.updateAccountName("Existing", 5, 1))
                .thenThrow(new AlreadyExistsException("The account with this name already exists"));

        mockMvc.perform(post("/api/account/update")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateAccount_notFound_404() throws Exception {
        AccountNameChangingRequest request = new AccountNameChangingRequest("NewName", 99);

        when(accountService.updateAccountName("NewName", 99, 1))
                .thenThrow(new NotFoundException("No account found with Id: 99"));

        mockMvc.perform(post("/api/account/update")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAccount_validationError_400() throws Exception {
        AccountNameChangingRequest request = new AccountNameChangingRequest("", 0);

        mockMvc.perform(post("/api/account/update")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

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