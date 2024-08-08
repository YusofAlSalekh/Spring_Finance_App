package ru.yusof.converter;

import ru.yusof.dao.TransactionModel;
import ru.yusof.service.TransactionDTO;

public class TransactionModelToTransactionDtoConverter<S, T> implements Converter<TransactionModel, TransactionDTO> {

    @Override
    public TransactionDTO convert(TransactionModel source) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(source.getId());
        transactionDTO.setCreatedDate(source.getCreatedDate());
        transactionDTO.setAmount(source.getAmount());
        transactionDTO.setSenderAccountId(source.getSenderAccountId());
        transactionDTO.setReceiverAccountId(source.getReceiverAccountId());

        return transactionDTO;
    }
}