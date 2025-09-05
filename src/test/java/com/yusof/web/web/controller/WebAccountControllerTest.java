package com.yusof.web.web.controller;

import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.exceptions.UnauthorizedException;
import com.yusof.web.service.AccountDTO;
import com.yusof.web.service.AccountService;
import com.yusof.web.web.form.AccountCreationForm;
import com.yusof.web.web.form.AccountDeletionForm;
import com.yusof.web.web.form.AccountNameChangingForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(MockSecurityConfig.class)
@WebMvcTest(WebAccountController.class)
class WebAccountControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AccountService accountService;

    @Test
    void getAccountCreation_form() throws Exception {
        mockMvc.perform(get("/account/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("form"))
                .andExpect(model().attribute("form", instanceOf(AccountCreationForm.class)))
                .andExpect(view().name("accountCreation"))
        ;
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postAccountCreation_success() throws Exception {
        AccountDTO accountDTO = new AccountDTO(1, "Account", new BigDecimal("100.00"), 1);
        when(accountService.createAccount("Account", new BigDecimal("100.00"), 1)).thenReturn(accountDTO);

        mockMvc.perform(post("/account/create")
                        .param("name", "Account")
                        .param("balance", "100.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/menu"))
                .andExpect(flash().attributeExists("success"));
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postAccountCreation_unauthorized_redirectsLogin() throws Exception {
        when(accountService.createAccount("Account", new BigDecimal("100.00"), 1))
                .thenThrow(new UnauthorizedException("Exception"));

        mockMvc.perform(post("/account/create")
                        .param("name", "Account")
                        .param("balance", "100.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postAccountCreation_alreadyExists_fieldError() throws Exception {
        when(accountService.createAccount("Account", new BigDecimal("100.00"), 1))
                .thenThrow(new AlreadyExistsException("Exception"));

        mockMvc.perform(post("/account/create")
                        .param("name", "Account")
                        .param("balance", "100.00"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountCreation"))
                .andExpect(model().attributeHasFieldErrors("form", "name"));
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postAccountCreation_validationErrors() throws Exception {
        mockMvc.perform(post("/account/create")
                        .param("name", "")
                        .param("balance", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("accountCreation"))
                .andExpect(model().attributeHasErrors("form"));
    }

    @Test
    void getAccountDeletion_form() throws Exception {
        mockMvc.perform(get("/account/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("form"))
                .andExpect(model().attribute("form", instanceOf(AccountDeletionForm.class)))
                .andExpect(view().name("accountDeletion"));
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postAccountDeletion_success() throws Exception {
        doNothing().when(accountService).deleteAccount(1, 1);

        mockMvc.perform(post("/account/delete")
                        .param("accountId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/menu"))
                .andExpect(flash().attributeExists("success"));
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postAccountDeletion_unauthorized_redirectsLogin() throws Exception {
        doThrow(new UnauthorizedException("Exception"))
                .when(accountService).deleteAccount(1, 1);

        mockMvc.perform(post("/account/delete")
                        .param("accountId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postAccountDeletion_notFound_globalError() throws Exception {
        doThrow(new NotFoundException("Exception"))
                .when(accountService).deleteAccount(1, 1);

        mockMvc.perform(post("/account/delete")
                        .param("accountId", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("form"))
                .andExpect(view().name("accountDeletion"));
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postAccountDeletion_validationErrors() throws Exception {
        mockMvc.perform(post("/account/delete")
                        .param("accountId", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("form", "accountId"))
                .andExpect(view().name("accountDeletion"));
    }

    @Test
    void getAccountNameChanging_form() throws Exception {
        mockMvc.perform(get("/account/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("form"))
                .andExpect(model().attribute("form", instanceOf(AccountNameChangingForm.class)))
                .andExpect(view().name("accountNameChanging"))
        ;
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postAccountNameChanging_success() throws Exception {
        when(accountService.updateAccountName("NewName", 1, 1))
                .thenReturn(new AccountDTO(1, "NewName", new BigDecimal("100.00"), 1));

        mockMvc.perform(post("/account/update")
                        .param("accountId", "1")
                        .param("name", "NewName"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/menu"))
                .andExpect(flash().attributeExists("success"));
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postAccountNameChanging_unauthorized_redirectsLogin() throws Exception {
        when(accountService.updateAccountName("NewName", 1, 1))
                .thenThrow(new UnauthorizedException("Exception"));

        mockMvc.perform(post("/account/update")
                        .param("accountId", "1")
                        .param("name", "NewName"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postAccountNameChanging_alreadyExists_globalError() throws Exception {
        when(accountService.updateAccountName("ExistedName", 1, 1))
                .thenThrow(new AlreadyExistsException("Exception"));

        mockMvc.perform(post("/account/update")
                        .param("accountId", "1")
                        .param("name", "ExistedName"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountNameChanging"))
                .andExpect(model().attributeHasErrors("form"));
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postAccountNameChanging_notFound_globalError() throws Exception {
        when(accountService.updateAccountName("NewName", 999, 1))
                .thenThrow(new NotFoundException("no such"));

        mockMvc.perform(post("/account/update")
                        .param("accountId", "999")
                        .param("name", "NewName"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountNameChanging"))
                .andExpect(model().attributeHasErrors("form"));
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postAccountNameChanging_validationErrors() throws Exception {
        mockMvc.perform(post("/account/update")
                        .param("accountId", "")
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("accountNameChanging"))
                .andExpect(model().attributeHasFieldErrors("form", "accountId"))
                .andExpect(model().attributeHasFieldErrors("form", "name"));
    }
}