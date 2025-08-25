package com.yusof.web.service;

import com.yusof.web.api.controller.TransactionCommandCreation;
import com.yusof.web.entity.AccountModel;
import com.yusof.web.entity.CategoryReportModel;
import com.yusof.web.entity.TransactionCategoryModel;
import com.yusof.web.entity.TransactionModel;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.repository.AccountRepository;
import com.yusof.web.repository.TransactionCategoryRepository;
import com.yusof.web.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @InjectMocks
    TransactionService subject;

    @Mock
    TransactionRepository transactionRepository;
    @Mock
    AccountRepository accountRepository;
    @Mock
    TransactionCategoryRepository transactionCategoryRepository;
    @Mock
    Converter<TransactionModel, TransactionDTO> transactionConverter;

    @Test
    void getIncomeReportByCategory_returnsRepositoryResult() {
        int clientId = 1;
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);
        BigDecimal totalAmount = new BigDecimal("100.00");

        List<CategoryReportModel> expected = List.of(new CategoryReportModel("Category", totalAmount));

        when(transactionRepository.fetchIncomeByCategory(
                clientId,
                start.atStartOfDay(),
                end.plusDays(1).atStartOfDay()
        )).thenReturn(expected);

        List<CategoryReportModel> result = subject.getIncomeReportByCategory(clientId, start, end);

        assertEquals(expected, result);
        verify(transactionRepository).fetchIncomeByCategory(
                clientId,
                start.atStartOfDay(),
                end.plusDays(1).atStartOfDay()
        );
    }

    @Test
    void getExpenseReportByCategory_returnsRepositoryResult() {
        int clientId = 1;
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);
        BigDecimal totalAmount = new BigDecimal("100.00");

        List<CategoryReportModel> expected = List.of(new CategoryReportModel("Category", totalAmount));

        when(transactionRepository.fetchExpenseByCategory(
                clientId,
                start.atStartOfDay(),
                end.plusDays(1).atStartOfDay()
        )).thenReturn(expected);

        List<CategoryReportModel> result = subject.getExpenseReportByCategory(clientId, start, end);

        assertEquals(expected, result);
        verify(transactionRepository).fetchExpenseByCategory(
                clientId,
                start.atStartOfDay(),
                end.plusDays(1).atStartOfDay()
        );
    }

    @Test
    void performTransaction_success() {
        int clientId = 100;
        int senderId = 1;
        int receiverId = 2;
        BigDecimal amount = new BigDecimal("150.00");
        List<Integer> categories = List.of(1);

        AccountModel sender = new AccountModel();
        sender.setId(senderId);
        sender.setClientId(clientId);
        sender.setBalance(new BigDecimal("500.00"));

        AccountModel receiver = new AccountModel();
        receiver.setId(receiverId);
        receiver.setBalance(new BigDecimal("200.00"));

        TransactionCategoryModel category = new TransactionCategoryModel();
        category.setId(1);

        when(accountRepository.findByIdAndClientId(senderId, clientId))
                .thenReturn(Optional.of(sender));
        when(accountRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(accountRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
        when(transactionCategoryRepository.findById(1)).thenReturn(Optional.of(category));

        TransactionCommandCreation command =
                new TransactionCommandCreation(senderId, receiverId, clientId, amount, categories);

        subject.performTransaction(command);

        assertEquals(new BigDecimal("350.00"), sender.getBalance());
        assertEquals(new BigDecimal("350.00"), receiver.getBalance());
        verify(transactionRepository).save(any(TransactionModel.class));
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void performTransaction_throwsIllegalArgumentException_dueTo_Insufficient_funds() {
        int clientId = 100;
        int senderId = 1;
        int receiverId = 2;
        BigDecimal amount = new BigDecimal("150.00");

        AccountModel sender = new AccountModel();
        sender.setId(senderId);
        sender.setClientId(clientId);
        sender.setBalance(new BigDecimal("50.00"));

        when(accountRepository.findByIdAndClientId(senderId, clientId))
                .thenReturn(Optional.of(sender));

        TransactionCommandCreation command =
                new TransactionCommandCreation(senderId, receiverId, clientId, amount, List.of());

        assertThrowsExactly(IllegalArgumentException.class, () -> subject.performTransaction(command));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void performTransaction_failsWhenReceiverNotFound() {
        int clientId = 1;
        int senderId = 1;
        int receiverId = 2;
        BigDecimal amount = new BigDecimal("100.00");

        AccountModel sender = new AccountModel();
        sender.setId(senderId);
        sender.setClientId(clientId);
        sender.setBalance(new BigDecimal("500.00"));

        when(accountRepository.findByIdAndClientId(senderId, clientId))
                .thenReturn(Optional.of(sender));
        when(accountRepository.findById(receiverId))
                .thenReturn(Optional.empty());

        TransactionCommandCreation command =
                new TransactionCommandCreation(senderId, receiverId, clientId, amount, List.of());

        assertThrowsExactly(NotFoundException.class, () -> subject.performTransaction(command));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void performTransaction_failsWhenCategoryNotFound() {
        int clientId = 1;
        int senderId = 1;
        int receiverId = 2;
        BigDecimal amount = new BigDecimal("100.00");
        int categoryId = 1;

        AccountModel sender = new AccountModel();
        sender.setId(senderId);
        sender.setClientId(clientId);
        sender.setBalance(new BigDecimal("500.00"));

        AccountModel receiver = new AccountModel();
        receiver.setId(receiverId);
        receiver.setBalance(new BigDecimal("0.00"));

        when(accountRepository.findByIdAndClientId(senderId, clientId))
                .thenReturn(Optional.of(sender));
        when(accountRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(accountRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
        when(transactionCategoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        TransactionCommandCreation command =
                new TransactionCommandCreation(senderId, receiverId, clientId, amount, List.of(categoryId));

        assertThrowsExactly(NotFoundException.class, () -> subject.performTransaction(command));
        verify(transactionRepository, never()).save(any());
    }
}