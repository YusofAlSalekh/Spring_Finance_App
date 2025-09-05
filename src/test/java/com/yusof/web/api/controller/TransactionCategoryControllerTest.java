package com.yusof.web.api.controller;

import com.yusof.web.entity.CategoryReportModel;
import com.yusof.web.service.TransactionCategoryDTO;
import com.yusof.web.service.TransactionCategoryService;
import com.yusof.web.service.TransactionService;
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
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(MockSecurityConfig.class)
@WebMvcTest(TransactionCategoryController.class)
class TransactionCategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TransactionService transactionService;

    @MockitoBean
    TransactionCategoryService transactionCategoryService;

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void createCategory_created() throws Exception {
        when(transactionCategoryService.createCategory("Food", 1))
                .thenReturn(new TransactionCategoryDTO(10, "Food", 1));

        String body = """
                  {"name":"Food"}
                """;

        mockMvc.perform(post("/api/category/create")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        verify(transactionCategoryService).createCategory("Food", 1);
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void createCategory_validationError_badRequest() throws Exception {
        String body = """
                  {"name":""}
                """;

        mockMvc.perform(post("/api/category/create")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(transactionCategoryService);
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void deleteCategory_noContent() throws Exception {
        String body = """
                  {"id":7}
                """;

        mockMvc.perform(post("/api/category/delete")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent());

        verify(transactionCategoryService).deleteTransactionCategory(7, 1);
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void deleteCategory_validationError_badRequest() throws Exception {
        String body = """
                  {"id":0}
                """;

        mockMvc.perform(post("/api/category/delete")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(transactionCategoryService);
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void updateCategory_ok_returnsDto() throws Exception {
        when(transactionCategoryService.updateTransactionCategory("Groceries", 5, 1))
                .thenReturn(new TransactionCategoryDTO(5, "Groceries", 1));

        String body = """
                  {"id":5,"name":"Groceries"}
                """;

        mockMvc.perform(post("/api/category/update")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        verify(transactionCategoryService).updateTransactionCategory("Groceries", 5, 1);
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void updateCategory_validationError_badRequest() throws Exception {
        String body = """
                  {"id":-1,"name":""}
                """;

        mockMvc.perform(post("/api/category/update")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(transactionCategoryService);
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void showCategories_ok_returnsList() throws Exception {
        when(transactionCategoryService.viewTransactionCategory(1))
                .thenReturn(List.of(
                        new TransactionCategoryDTO(1, "Food", 1),
                        new TransactionCategoryDTO(2, "Transport", 1)
                ));

        mockMvc.perform(get("/api/category/show").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Food"))
                .andExpect(jsonPath("$[1].name").value("Transport"));

        verify(transactionCategoryService).viewTransactionCategory(1);
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void incomeReport_ok_returnsList() throws Exception {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 31);

        when(transactionService.getIncomeReportByCategory(1, start, end))
                .thenReturn(List.of(
                        new CategoryReportModel("Salary", new BigDecimal("1000.00")),
                        new CategoryReportModel("Bonus", new BigDecimal("250.00"))
                ));

        String body = """
                  {"startDate":"2025-01-01",
                  "endDate":"2025-01-31"}
                """;

        mockMvc.perform(post("/api/category/report/income")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].categoryName").value("Salary"))
                .andExpect(jsonPath("$[0].totalAmount").value(1000.00))
                .andExpect(jsonPath("$[1].categoryName").value("Bonus"))
                .andExpect(jsonPath("$[1].totalAmount").value(250.00));

        verify(transactionService).getIncomeReportByCategory(1, start, end);
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void expenseReport_ok_returnsList() throws Exception {
        LocalDate start = LocalDate.of(2025, 2, 1);
        LocalDate end = LocalDate.of(2025, 2, 28);

        when(transactionService.getExpenseReportByCategory(eq(1), eq(start), eq(end)))
                .thenReturn(List.of(
                        new CategoryReportModel("Food", new BigDecimal("120.50")),
                        new CategoryReportModel("Fuel", new BigDecimal("80.00"))
                ));

        String body = """
                  {"startDate":"2025-02-01","endDate":"2025-02-28"}
                """;

        mockMvc.perform(post("/api/category/report/expense")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(transactionService).getExpenseReportByCategory(1, start, end);
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void report_validationError_badRequest_income() throws Exception {
        mockMvc.perform(post("/api/category/report/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(transactionService);
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void report_validationError_badRequest_expense() throws Exception {
        mockMvc.perform(post("/api/category/report/expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(transactionService);
    }
}