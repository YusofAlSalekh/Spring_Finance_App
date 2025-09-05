package com.yusof.web.api.controller;

import com.yusof.web.service.TransactionService;
import com.yusof.web.web.controller.MockSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(MockSecurityConfig.class)
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TransactionService transactionService;

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void createTransaction_success() throws Exception {

        doNothing().when(transactionService).performTransaction(any());

        String body = """
                  {
                    "categoryIds":[1,2],
                    "amount": 150.25,
                    "senderAccountId": 10,
                    "receiverAccountId": 11
                  }
                """;

        mockMvc.perform(post("/api/transaction/create")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @WithUserDetails(value = "user@gmail.com",
            userDetailsServiceBeanName = "userDetailsService")
    @Test
    void createTransaction_validationErrors_returns400() throws Exception {
        String invalidBody = """
                  {
                    "categoryIds":[-1,0],
                    "amount": -5,
                    "senderAccountId": 10,
                    "receiverAccountId": 11
                  }
                """;

        mockMvc.perform(post("/api/transaction/create")
                        .contentType(APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }
}