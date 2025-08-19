package ru.yusof.converter;

import org.springframework.stereotype.Service;
import ru.yusof.entity.TransactionModel;
import ru.yusof.service.TransactionDTO;

@Service
public class TransactionModelToTransactionDtoConverter<S, T> implements Converter<TransactionModel, TransactionDTO> {

    @Override
    public TransactionDTO convert(TransactionModel source) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(source.getId());
        transactionDTO.setCreatedDate(source.getCreatedDate());
        transactionDTO.setAmount(source.getAmount());
        transactionDTO.setSenderAccountId(source.getSender().getId());
        transactionDTO.setReceiverAccountId(source.getReceiver().getId());

        return transactionDTO;
    }
}