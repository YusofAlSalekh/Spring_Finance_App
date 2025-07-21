package ru.yusof.service;

import org.springframework.stereotype.Service;
import ru.yusof.converter.Converter;
import ru.yusof.dao.TransactionCategoryDao;
import ru.yusof.entity.TransactionCategoryModel;

import java.util.ArrayList;
import java.util.List;

@Service
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
        return transactionCategoryDao.updateTransactionCategoryName(newName, transactionCategoryId, clientId);
    }

    public List<TransactionCategoryDTO> viewTransactionCategory(int clientId) {
        List<TransactionCategoryModel> accountModels = transactionCategoryDao.findByClientID(clientId);
        List<TransactionCategoryDTO> transactionCategoryDTOs = new ArrayList<>();

        for (TransactionCategoryModel transactionCategoryModel : accountModels) {
            transactionCategoryDTOs.add(transactionCategoryDTOConverter.convert(transactionCategoryModel));
        }
        return transactionCategoryDTOs;
    }
}
