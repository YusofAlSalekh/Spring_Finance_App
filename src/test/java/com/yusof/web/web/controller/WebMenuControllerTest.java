package com.yusof.web.web.controller;

import com.yusof.web.service.AccountDTO;
import com.yusof.web.service.AccountService;
import com.yusof.web.service.TransactionCategoryDTO;
import com.yusof.web.service.TransactionCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebMenuController.class)
@Import(MockSecurityConfig.class)
@WithUserDetails(
        value = "user@gmail.com",
        userDetailsServiceBeanName = "userDetailsService"
)
class WebMenuControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AccountService accountService;

    @MockitoBean
    TransactionCategoryService transactionCategoryService;

    @Test
    void getMenu_showAccounts_loadsAccounts() throws Exception {
        List<AccountDTO> accounts = List.of(
                new AccountDTO(1, "accountOne", new BigDecimal("100.00"), 1),
                new AccountDTO(2, "accountTwo", new BigDecimal("20.00"), 1)
        );

        when(accountService.viewAccount(1)).thenReturn(accounts);

        mockMvc.perform(get("/menu").param("show", "accounts"))
                .andExpect(status().isOk())
                .andExpect(view().name("menu"))
                .andExpect(model().attribute("accounts", accounts))
                .andExpect(model().attribute("id", 1))
                .andExpect(model().attribute("name", "user@gmail.com"))
                .andExpect(model().attributeDoesNotExist("categories"));
    }

    @Test
    void getMenu_default_showsIdAndName_only() throws Exception {
        mockMvc.perform(get("/menu"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("id", 1))
                .andExpect(model().attribute("name", "user@gmail.com"))
                .andExpect(view().name("menu"))
                .andExpect(model().attributeDoesNotExist("accounts", "categories"));
    }

    @Test
    void getMenu_showCategories_loadsCategories() throws Exception {
        List<TransactionCategoryDTO> categories = List.of(new TransactionCategoryDTO(1, "transactionOne", 1));
        when(transactionCategoryService.viewTransactionCategory(1)).thenReturn(categories);

        mockMvc.perform(get("/menu").param("show", "categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("menu"))
                .andExpect(model().attribute("categories", categories))
                .andExpect(model().attribute("id", 1))
                .andExpect(model().attribute("name", "user@gmail.com"))
                .andExpect(model().attributeDoesNotExist("accounts"));
    }
}