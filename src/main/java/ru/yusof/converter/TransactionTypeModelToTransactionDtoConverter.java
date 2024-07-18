package ru.yusof.converter;

import ru.yusof.dao.TransactionTypeModel;
import ru.yusof.service.TransactionDTO;

public class TransactionTypeModelToTransactionDtoConverter<S, T> implements Converter<TransactionTypeModel, TransactionDTO> {

    @Override
    public TransactionDTO convert(TransactionTypeModel source) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(source.getId());
        transactionDTO.setName(source.getName());
        transactionDTO.setClient_id(source.getClientId());
        return transactionDTO;
    }
}