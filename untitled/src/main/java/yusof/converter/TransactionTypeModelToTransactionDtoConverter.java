package yusof.converter;

import yusof.dao.TransactionTypeModel;
import yusof.service.TransactionTypeDTO;

public class TransactionTypeModelToTransactionDtoConverter<S, T> implements Converter<TransactionTypeModel, TransactionTypeDTO> {
    @Override
    public TransactionTypeDTO convert(TransactionTypeModel source) {
        TransactionTypeDTO transactionTypeDTO = new TransactionTypeDTO();
        transactionTypeDTO.setId(source.getId());
        transactionTypeDTO.setName(source.getName());
        transactionTypeDTO.setClient_id(source.getClientId());
        return transactionTypeDTO;
    }
}
