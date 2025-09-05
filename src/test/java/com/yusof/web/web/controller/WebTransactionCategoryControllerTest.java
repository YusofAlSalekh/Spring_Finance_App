package com.yusof.web.web.controller;

import com.yusof.web.entity.CategoryReportModel;
import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.exceptions.UnauthorizedException;
import com.yusof.web.service.TransactionCategoryDTO;
import com.yusof.web.service.TransactionCategoryService;
import com.yusof.web.service.TransactionService;
import com.yusof.web.web.form.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(MockSecurityConfig.class)
@WebMvcTest(WebTransactionCategoryController.class)
class WebTransactionCategoryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TransactionCategoryService transactionCategoryService;

    @MockitoBean
    TransactionService transactionService;


    @Test
    void getTransactionCategoryCreation() throws Exception {
        mockMvc.perform(get("/category/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("form"))
                .andExpect(model().attribute("form", instanceOf(TransactionCategoryCreationForm.class)))
                .andExpect(view().name("transactionCategoryCreation"));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postCategoryCreation_success() throws Exception {
        TransactionCategoryDTO transactionCategory = new TransactionCategoryDTO(1, "CategoryName", 1);
        when(transactionCategoryService.createCategory("CategoryName", 1))
                .thenReturn(transactionCategory);

        mockMvc.perform(post("/category/create")
                        .param("name", "CategoryName"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/menu"))
                .andExpect(flash().attributeExists("success"));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postCategoryCreation_unauthorized_redirectsLogin() throws Exception {
        when(transactionCategoryService.createCategory("CategoryName", 1))
                .thenThrow(new UnauthorizedException("Exception"));

        mockMvc.perform(post("/category/create")
                        .param("name", "CategoryName"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postCategoryCreation_alreadyExists_fieldError() throws Exception {
        when(transactionCategoryService.createCategory("CategoryName", 1))
                .thenThrow(new AlreadyExistsException("exists"));

        mockMvc.perform(post("/category/create")
                        .param("name", "CategoryName"))
                .andExpect(status().isOk())
                .andExpect(view().name("transactionCategoryCreation"))
                .andExpect(model().attributeHasFieldErrors("form", "name"));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postCategoryCreation_validationErrors() throws Exception {
        mockMvc.perform(post("/category/create")
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("transactionCategoryCreation"))
                .andExpect(model().attributeHasErrors("form"));
    }

    @Test
    void getTransactionCategoryDeletion() throws Exception {
        mockMvc.perform(get("/category/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("transactionCategoryDeletion"))
                .andExpect(model().attributeExists("form"))
                .andExpect(model().attribute("form", instanceOf(TransactionCategoryDeletionForm.class)));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postCategoryDeletion_success() throws Exception {
        mockMvc.perform(post("/category/delete")
                        .param("id", "7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/menu"))
                .andExpect(flash().attributeExists("success"));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postCategoryDeletion_unauthorized_redirectsLogin() throws Exception {
        doThrow(new UnauthorizedException("Exception"))
                .when(transactionCategoryService).deleteTransactionCategory(7, 1);

        mockMvc.perform(post("/category/delete")
                        .param("id", "7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postCategoryDeletion_notFound_globalError() throws Exception {
        doThrow(new NotFoundException("Exception"))
                .when(transactionCategoryService).deleteTransactionCategory(999, 1);

        mockMvc.perform(post("/category/delete")
                        .param("id", "999"))
                .andExpect(status().isOk())
                .andExpect(view().name("transactionCategoryDeletion"))
                .andExpect(model().attributeHasErrors("form"));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postCategoryDeletion_validationErrors() throws Exception {
        mockMvc.perform(post("/category/delete")
                        .param("id", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("transactionCategoryDeletion"))
                .andExpect(model().attributeHasErrors("form"))
                .andExpect(model().attributeHasFieldErrors("form", "id"));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postCategoryUpdating_success() throws Exception {
        mockMvc.perform(post("/category/update")
                        .param("id", "10")
                        .param("name", "NewName"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/menu"))
                .andExpect(flash().attributeExists("success"));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postCategoryUpdating_unauthorized_redirectsLogin() throws Exception {
        doThrow(new UnauthorizedException("Exception"))
                .when(transactionCategoryService).updateTransactionCategory("NewName", 10, 1);

        mockMvc.perform(post("/category/update")
                        .param("id", "10")
                        .param("name", "NewName"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postCategoryUpdating_alreadyExists_globalError() throws Exception {
        doThrow(new AlreadyExistsException("Exception"))
                .when(transactionCategoryService).updateTransactionCategory("NewName", 10, 1);

        mockMvc.perform(post("/category/update")
                        .param("id", "10")
                        .param("name", "NewName"))
                .andExpect(status().isOk())
                .andExpect(view().name("transactionCategoryUpdating"))
                .andExpect(model().attributeHasErrors("form"));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postCategoryUpdating_notFound_globalError() throws Exception {
        doThrow(new NotFoundException("Exception"))
                .when(transactionCategoryService).updateTransactionCategory("NewName", 999, 1);

        mockMvc.perform(post("/category/update")
                        .param("id", "999")
                        .param("name", "NewName"))
                .andExpect(status().isOk())
                .andExpect(view().name("transactionCategoryUpdating"))
                .andExpect(model().attributeHasErrors("form"));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postCategoryUpdating_validationErrors() throws Exception {
        mockMvc.perform(post("/category/update")
                        .param("id", "-2")
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("transactionCategoryUpdating"))
                .andExpect(model().attributeHasErrors("form"))
                .andExpect(model().attributeHasFieldErrors("form", "id"))
                .andExpect(model().attributeHasFieldErrors("form", "name"));
    }

    @Test
    void getTransactionCategoryUpdating() throws Exception {
        mockMvc.perform(get("/category/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("transactionCategoryUpdating"))
                .andExpect(model().attributeExists("form"))
                .andExpect(model().attribute("form", instanceOf(TransactionCategoryUpdatingForm.class)));
    }

    @Test
    void getExpenseReport() throws Exception {
        mockMvc.perform(get("/category/report/expense"))
                .andExpect(status().isOk())
                .andExpect(view().name("expenseReport"))
                .andExpect(model().attributeExists("form"))
                .andExpect(model().attribute("form", instanceOf(ExpenseReportForm.class)));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postExpenseReport_success() throws Exception {
        LocalDate start = LocalDate.parse("2025-01-01");
        LocalDate end = LocalDate.parse("2025-01-31");

        List<CategoryReportModel> transactions = List.of(
                new CategoryReportModel("Food", new BigDecimal("100.00")),
                new CategoryReportModel("Travel", new BigDecimal("70.00"))
        );
        when(transactionService.getExpenseReportByCategory(1, start, end))
                .thenReturn(transactions);

        mockMvc.perform(post("/category/report/expense")
                        .param("startDate", "2025-01-01")
                        .param("endDate", "2025-01-31"))
                .andExpect(status().isOk())
                .andExpect(view().name("expenseReport"))
                .andExpect(model().attributeExists("transactions"));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postExpenseReport_validationErrors() throws Exception {
        mockMvc.perform(post("/category/report/expense")
                        .param("startDate", "")
                        .param("endDate", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("expenseReport"))
                .andExpect(model().attributeHasErrors("form"))
                .andExpect(model().attributeHasFieldErrors("form", "startDate"))
                .andExpect(model().attributeHasFieldErrors("form", "endDate"));
    }

    @Test
    void getIncomeReport() throws Exception {
        mockMvc.perform(get("/category/report/income"))
                .andExpect(status().isOk())
                .andExpect(view().name("incomeReport"))
                .andExpect(model().attributeExists("form"))
                .andExpect(model().attribute("form", instanceOf(IncomeReportForm.class)));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postIncomeReport_success() throws Exception {
        LocalDate start = LocalDate.parse("2025-01-01");
        LocalDate end = LocalDate.parse("2025-01-31");

        List<CategoryReportModel> transactions = List.of(
                new CategoryReportModel("Food", new BigDecimal("100.00")),
                new CategoryReportModel("Travel", new BigDecimal("70.00"))
        );
        when(transactionService.getIncomeReportByCategory(1, start, end))
                .thenReturn(transactions);

        mockMvc.perform(post("/category/report/income")
                        .param("startDate", "2025-01-01")
                        .param("endDate", "2025-01-31"))
                .andExpect(status().isOk())
                .andExpect(view().name("incomeReport"))
                .andExpect(model().attributeExists("transactions"));
    }

    @WithUserDetails(
            value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    @Test
    void postIncomeReport_validationErrors() throws Exception {
        mockMvc.perform(post("/category/report/income")
                        .param("startDate", "")
                        .param("endDate", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("incomeReport"))
                .andExpect(model().attributeHasErrors("form"))
                .andExpect(model().attributeHasFieldErrors("form", "endDate"))
                .andExpect(model().attributeHasFieldErrors("form", "startDate"));
    }
}