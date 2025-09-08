package com.yusof.web.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yusof.web.api.json.request.TransactionCreationRequest;
import com.yusof.web.service.TransactionService;
import com.yusof.web.web.controller.MockSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(MockSecurityConfig.class)
@WebMvcTest(TransactionController.class)
@WithUserDetails(value = "user@gmail.com",
        userDetailsServiceBeanName = "userDetailsService")
class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TransactionService transactionService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createTransaction_success() throws Exception {
        TransactionCreationRequest request =
                new TransactionCreationRequest(List.of(1, 2),
                        new BigDecimal("150.25"),
                        10,
                        11);

        doNothing().when(transactionService).performTransaction(any());

        mockMvc.perform(post("/api/transaction/create")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createTransaction_validationErrors_returns400() throws Exception {
        TransactionCreationRequest request =
                new TransactionCreationRequest(List.of(-1, 0),
                        new BigDecimal("-5.25"),
                        10,
                        11);

        mockMvc.perform(post("/api/transaction/create")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}