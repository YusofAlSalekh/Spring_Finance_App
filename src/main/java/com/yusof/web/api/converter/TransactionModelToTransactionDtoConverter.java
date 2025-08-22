package com.yusof.web.api.converter;

import com.yusof.web.entity.TransactionModel;
import com.yusof.web.service.TransactionDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class TransactionModelToTransactionDtoConverter implements Converter<TransactionModel, TransactionDTO> {

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