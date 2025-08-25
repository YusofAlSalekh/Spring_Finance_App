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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Validated
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;

    public List<CategoryReportModel> getIncomeReportByCategory(int clientId, @NotNull @PastOrPresent LocalDate start, @NotNull @PastOrPresent LocalDate end) {
        return transactionRepository.fetchIncomeByCategory(clientId, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
    }

    public List<CategoryReportModel> getExpenseReportByCategory(int clientId, @NotNull @PastOrPresent LocalDate start, @NotNull @PastOrPresent LocalDate end) {
        return transactionRepository.fetchExpenseByCategory(clientId, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
    }

    @Transactional
    public void performTransaction(@Valid TransactionCommandCreation command) {

        checkBalanceSufficiency(command.getSenderAccountId(), command.getClientId(), command.getAmount());

        subtractMoneyFromSender(command.getSenderAccountId(), command.getClientId(), command.getAmount());

        addMoneyToReceiver(command.getReceiverAccountId(), command.getAmount());

        createTransaction(command.getSenderAccountId(), command.getReceiverAccountId(), command.getAmount(), command.getCategoryIds());
    }

    private void checkBalanceSufficiency(int senderAccountId, int clientId, BigDecimal amount) {

        AccountModel senderAccount = getSenderAccountModel(senderAccountId, clientId);

        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds in sender's account with id " + senderAccountId);
        }
    }

    private void subtractMoneyFromSender(int senderAccountId, int clientId, BigDecimal amount) {
        AccountModel senderAccount = getSenderAccountModel(senderAccountId, clientId);

        BigDecimal newBalance = senderAccount.getBalance().subtract(amount);
        senderAccount.setBalance(newBalance);
    }

    private AccountModel getSenderAccountModel(int senderAccountId, int clientId) {
        return accountRepository.findByIdAndClientId(senderAccountId, clientId)
                .orElseThrow(() -> new NotFoundException("Account with id " + senderAccountId + " not found"));
    }

    private void addMoneyToReceiver(int receiverAccountId, BigDecimal amount) {
        AccountModel receiverAccount = accountRepository.findById(receiverAccountId)
                .orElseThrow(() -> new NotFoundException("Account with id " + receiverAccountId + " not found"));

        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));
    }

    private void createTransaction(int senderAccountId, int receiverAccountId, BigDecimal amount, List<Integer> categoryIds) {
        AccountModel senderAccount = accountRepository.findById(senderAccountId)
                .orElseThrow(() -> new NotFoundException("Account with id " + senderAccountId + " not found"));

        AccountModel receiverAccount = accountRepository.findById(receiverAccountId)
                .orElseThrow(() -> new NotFoundException("Account with id " + receiverAccountId + " not found"));

        List<TransactionCategoryModel> categoryModels = getTransactionCategoryModels(categoryIds);

        TransactionModel transactionModel = buildTransaction(amount, receiverAccount, senderAccount, categoryModels);

        transactionRepository.save(transactionModel);
    }

    private List<TransactionCategoryModel> getTransactionCategoryModels(List<Integer> categoryIds) {
        return categoryIds.stream()
                .map(categoryId -> transactionCategoryRepository.findById(categoryId)
                        .orElseThrow(() -> new NotFoundException("Category with id " + categoryId + " not found")))
                .collect(Collectors.toList());
    }

    private static TransactionModel buildTransaction(BigDecimal amount, AccountModel receiverAccount, AccountModel senderAccount, List<TransactionCategoryModel> categoryModels) {
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setAmount(amount);
        transactionModel.setCreatedDate(LocalDateTime.now());
        transactionModel.setReceiver(receiverAccount);
        transactionModel.setSender(senderAccount);
        transactionModel.setCategories(categoryModels);
        return transactionModel;
    }
}