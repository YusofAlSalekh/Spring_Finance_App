package yusof.service;

import yusof.converter.TransactionTypeModelToTransactionDtoConverter;
import yusof.dao.CategoryAmountModel;
import yusof.dao.TransactionTypeDao;
import yusof.dao.TransactionTypeModel;

import java.time.LocalDate;
import java.util.List;

public class TransactionTypeService {
    private final TransactionTypeDao transactionTypeDao;
    private final TransactionTypeModelToTransactionDtoConverter transactionTypeModelToTransactionDtoConverter;

    public TransactionTypeService(TransactionTypeDao transactionTypeDao, TransactionTypeModelToTransactionDtoConverter transactionTypeModelToTransactionDtoConverter) {
        this.transactionTypeDao = transactionTypeDao;
        this.transactionTypeModelToTransactionDtoConverter = transactionTypeModelToTransactionDtoConverter;
    }

    public TransactionTypeDTO createCategory(String categoryName, int clientId) {
        TransactionTypeModel transactionTypeModel = transactionTypeDao.createTransactionType(categoryName, clientId);
        return transactionTypeModelToTransactionDtoConverter.convert(transactionTypeModel);
    }

    public boolean deleteTransactionType(int transactionTypeId, int clientId) {
        return transactionTypeDao.deleteTransactionType(transactionTypeId, clientId);
    }

    public boolean editTransactionType(String newName, int transactionTypeId, int clientId) {
        return transactionTypeDao.editTransactionType(newName, transactionTypeId, clientId);
    }

    public List<CategoryAmountModel> getIncomeReportByCategory(int clientId, LocalDate start, LocalDate end) {
        return transactionTypeDao.fetchIncomeByCategory(clientId, start, end);
    }

    public List<CategoryAmountModel> getExpenseReportByCategory(int clientId, LocalDate start, LocalDate end) {
        return transactionTypeDao.fetchExpenseByCategory(clientId, start, end);
    }

}