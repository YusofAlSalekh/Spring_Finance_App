package ru.yusof.service;

import org.springframework.stereotype.Service;
import ru.yusof.converter.Converter;
import ru.yusof.dao.TransactionDao;
import ru.yusof.entity.CategoryAmountModel;
import ru.yusof.entity.TransactionModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionDao transactionDao;
    private final Converter<TransactionModel, TransactionDTO> transactionModelToTransactionDtoConverter;

    public TransactionService(TransactionDao transactionDao, Converter<TransactionModel, TransactionDTO> transactionModelToTransactionDtoConverter) {
        this.transactionDao = transactionDao;
        this.transactionModelToTransactionDtoConverter = transactionModelToTransactionDtoConverter;
    }

    public List<CategoryAmountModel> getIncomeReportByCategory(int clientId, LocalDate start, LocalDate end) {
        return transactionDao.fetchIncomeByCategory(clientId, start, end);
    }

    public List<CategoryAmountModel> getExpenseReportByCategory(int clientId, LocalDate start, LocalDate end) {
        return transactionDao.fetchExpenseByCategory(clientId, start, end);
    }

    public void performTransaction(int senderAccountId, int receiverAccountId, int clientId, BigDecimal amount, List<Integer> categoryIds) {
        transactionDao.addTransaction(senderAccountId, receiverAccountId, clientId, amount, categoryIds);
    }
}