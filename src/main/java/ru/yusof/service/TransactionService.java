package ru.yusof.service;

import ru.yusof.converter.Converter;
import ru.yusof.dao.CategoryAmountModel;
import ru.yusof.dao.TransactionDao;
import ru.yusof.dao.TransactionTypeModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransactionService {
    private final TransactionDao transactionDao;
    private final Converter<TransactionTypeModel, TransactionDTO> transactionTypeModelToTransactionDtoConverter;

    public TransactionService(TransactionDao transactionDao, Converter<TransactionTypeModel, TransactionDTO> transactionTypeModelToTransactionDtoConverter) {
        this.transactionDao = transactionDao;
        this.transactionTypeModelToTransactionDtoConverter = transactionTypeModelToTransactionDtoConverter;
    }

    public TransactionDTO createCategory(String categoryName, int clientId) {
        TransactionTypeModel transactionTypeModel = transactionDao.createTransactionType(categoryName, clientId);
        return transactionTypeModelToTransactionDtoConverter.convert(transactionTypeModel);
    }

    public boolean deleteTransactionType(int transactionTypeId, int clientId) {
        return transactionDao.deleteTransactionType(transactionTypeId, clientId);
    }

    public boolean editTransactionType(String newName, int transactionTypeId, int clientId) {
        return transactionDao.editTransactionType(newName, transactionTypeId, clientId);
    }

    public List<CategoryAmountModel> getIncomeReportByCategory(int clientId, LocalDate start, LocalDate end) {
        return transactionDao.fetchIncomeByCategory(clientId, start, end);
    }

    public List<CategoryAmountModel> getExpenseReportByCategory(int clientId, LocalDate start, LocalDate end) {
        return transactionDao.fetchExpenseByCategory(clientId, start, end);
    }

    public void performTransaction(int senderAccountId, int receiverAccountId, BigDecimal amount, List<Integer> categoryIds) {
        transactionDao.addTransaction(senderAccountId, receiverAccountId, amount, categoryIds);
    }
}