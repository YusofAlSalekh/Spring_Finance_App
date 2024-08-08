package ru.yusof.service;

import ru.yusof.converter.Converter;
import ru.yusof.dao.TransactionCategoryDao;
import ru.yusof.dao.TransactionCategoryModel;

public class TransactionCategoryService {
    private final TransactionCategoryDao transactionCategoryDao;
    private final Converter<TransactionCategoryModel, TransactionCategoryDTO> transactionCategoryDTOConverter;

    public TransactionCategoryService(TransactionCategoryDao transactionCategoryDao, Converter<TransactionCategoryModel, TransactionCategoryDTO> transactionCategoryDTOConverter) {
        this.transactionCategoryDao = transactionCategoryDao;
        this.transactionCategoryDTOConverter = transactionCategoryDTOConverter;
    }

    public TransactionCategoryDTO createCategory(String categoryName, int clientId) {
        TransactionCategoryModel transactionCategoryModel = transactionCategoryDao.createTransactionCategory(categoryName, clientId);
        return transactionCategoryDTOConverter.convert(transactionCategoryModel);
    }

    public boolean deleteTransactionCategory(int transactionCategoryId, int clientId) {
        return transactionCategoryDao.deleteTransactionCategory(transactionCategoryId, clientId);
    }

    public boolean editTransactionCategory(String newName, int transactionCategoryId, int clientId) {
        return transactionCategoryDao.editTransactionCategory(newName, transactionCategoryId, clientId);
    }
}
