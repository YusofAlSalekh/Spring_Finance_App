package com.yusof.web.service;

import com.yusof.web.api.controller.TransactionCommandCreation;
import com.yusof.web.entity.AccountModel;
import com.yusof.web.entity.CategoryAmountModel;
import com.yusof.web.entity.TransactionCategoryModel;
import com.yusof.web.entity.TransactionModel;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.repository.AccountRepository;
import com.yusof.web.repository.TransactionCategoryRepository;
import com.yusof.web.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final Converter<TransactionModel, TransactionDTO> transactionModelToTransactionDtoConverter;

    public List<CategoryAmountModel> getIncomeReportByCategory(int clientId, LocalDate start, LocalDate end) {
        return transactionRepository.fetchIncomeByCategory(clientId, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
    }

    public List<CategoryAmountModel> getExpenseReportByCategory(int clientId, LocalDate start, LocalDate end) {
        return transactionRepository.fetchExpenseByCategory(clientId, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
    }

    @Transactional
    public void performTransaction(TransactionCommandCreation command) {

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
        return accountRepository.findAccountByIdAndClientId(senderAccountId, clientId)
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
        List<TransactionCategoryModel> categoryModels = new ArrayList<>();
        for (Integer categoryId : categoryIds) {
            TransactionCategoryModel transactionCategoryModel = transactionCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category with id " + categoryId + " not found"));
            categoryModels.add(transactionCategoryModel);
        }
        return categoryModels;
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