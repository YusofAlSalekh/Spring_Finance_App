package yusof.service;

import yusof.converter.TransactionTypeModelToTransactionDtoConverter;
import yusof.dao.TransactionTypeDao;
import yusof.dao.TransactionTypeModel;

public class TransactionTypeService {
    private final TransactionTypeDao transactionTypeDao;
    private final TransactionTypeModelToTransactionDtoConverter transactionTypeModelToTransactionDtoConverter;

    public TransactionTypeService() {
        this.transactionTypeDao = new TransactionTypeDao();
        this.transactionTypeModelToTransactionDtoConverter = new TransactionTypeModelToTransactionDtoConverter();
    }

    public TransactionTypeDTO createCategory(String categoryName, int clientId) {
        TransactionTypeModel transactionTypeModel = transactionTypeDao.createTransactionType(categoryName, clientId);

        if (transactionTypeModel == null) {
            return null;
        } else return transactionTypeModelToTransactionDtoConverter.convert(transactionTypeModel);
    }
}