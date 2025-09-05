package com.yusof.web.web.controller;

import com.yusof.web.api.controller.TransactionCommandCreation;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.service.TransactionCategoryDTO;
import com.yusof.web.service.TransactionCategoryService;
import com.yusof.web.service.TransactionService;
import com.yusof.web.web.form.TransactionCreationForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(MockSecurityConfig.class)
@WebMvcTest(WebTransactionController.class)
class WebTransactionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TransactionCategoryService transactionCategoryService;

    @MockitoBean
    TransactionService transactionService;

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void getCategories() throws Exception {
        when(transactionCategoryService.viewTransactionCategory(1))
                .thenReturn(List.of(new TransactionCategoryDTO(42, "Food", 1)));

        mockMvc.perform(get("/transaction/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("transactionCreation"))
                .andExpect(model().attributeExists("form"))
                .andExpect(model().attribute("form", instanceOf(TransactionCreationForm.class)))
                .andExpect(model().attributeExists("categories"));

        verify(transactionCategoryService).viewTransactionCategory(1);
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postTransactionCreation_success_redirectsMenu() throws Exception {
        when(transactionCategoryService.viewTransactionCategory(1))
                .thenReturn(List.of(
                        new TransactionCategoryDTO(42, "Food", 1),
                        new TransactionCategoryDTO(37, "Travel", 1)));

        mockMvc.perform(post("/transaction/create")
                        .param("senderAccountId", "10")
                        .param("receiverAccountId", "11")
                        .param("amount", "250")
                        .param("categoryIds", "42", "37"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/menu"))
                .andExpect(flash().attributeExists("success"));

        verify(transactionCategoryService).viewTransactionCategory(1);
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postTransactionCreation_categoryNotFound_fieldError() throws Exception {
        when(transactionCategoryService.viewTransactionCategory(1))
                .thenReturn(List.of(new TransactionCategoryDTO(1, "Food", 1)));


        doThrow(new NotFoundException("Category not found"))
                .when(transactionService).performTransaction(any(TransactionCommandCreation.class));

        mockMvc.perform(post("/transaction/create")
                        .param("senderAccountId", "10")
                        .param("receiverAccountId", "11")
                        .param("amount", "250")
                        .param("categoryIds", "999"))
                .andExpect(status().isOk())
                .andExpect(view().name("transactionCreation"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeHasFieldErrors("form", "categoryIds"));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postTransactionCreation_illegalArgument_globalError() throws Exception {
        when(transactionCategoryService.viewTransactionCategory(1))
                .thenReturn(List.of(new TransactionCategoryDTO(1, "Food", 1)));

        doThrow(new IllegalArgumentException("Amount must be positive"))
                .when(transactionService).performTransaction(any(TransactionCommandCreation.class));

        mockMvc.perform(post("/transaction/create")
                        .param("senderAccountId", "10")
                        .param("receiverAccountId", "11")
                        .param("amount", "0")
                        .param("categoryIds", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("transactionCreation"))
                .andExpect(model().attributeHasErrors("form"))
                .andExpect(model().attributeExists("categories"));

        verify(transactionCategoryService).viewTransactionCategory(1);
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postTransactionCreation_validationErrors_keepForm() throws Exception {
        when(transactionCategoryService.viewTransactionCategory(1))
                .thenReturn(List.of(new TransactionCategoryDTO(1, "Food", 42)));

        mockMvc.perform(post("/transaction/create")
                        .param("senderAccountId", " ")
                        .param("receiverAccountId", "")
                        .param("amount", "-300")
                        .param("categoryIds", "0", "-2"))
                .andExpect(status().isOk())
                .andExpect(view().name("transactionCreation"))
                .andExpect(model().attributeHasErrors("form"))
                .andExpect(model().attributeHasFieldErrors("form", "senderAccountId"))
                .andExpect(model().attributeHasFieldErrors("form", "receiverAccountId"))
                .andExpect(model().attributeHasFieldErrors("form", "amount"))
                .andExpect(model().attributeHasFieldErrors("form", "categoryIds[0]", "categoryIds[1]"))
                .andExpect(model().attributeExists("categories"));

        verify(transactionCategoryService).viewTransactionCategory(1);
    }
}