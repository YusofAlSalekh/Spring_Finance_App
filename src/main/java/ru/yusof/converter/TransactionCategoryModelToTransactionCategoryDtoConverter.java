package ru.yusof.converter;

import ru.yusof.dao.TransactionCategoryModel;
import ru.yusof.service.TransactionCategoryDTO;


public class TransactionCategoryModelToTransactionCategoryDtoConverter<S, T> implements Converter<TransactionCategoryModel, TransactionCategoryDTO> {

    @Override
    public TransactionCategoryDTO convert(TransactionCategoryModel source) {
        TransactionCategoryDTO transactionCategoryDTO = new TransactionCategoryDTO();
        transactionCategoryDTO.setId(source.getId());
        transactionCategoryDTO.setName(source.getName());
        transactionCategoryDTO.setClient_id(source.getClientId());
        return transactionCategoryDTO;
    }
}
