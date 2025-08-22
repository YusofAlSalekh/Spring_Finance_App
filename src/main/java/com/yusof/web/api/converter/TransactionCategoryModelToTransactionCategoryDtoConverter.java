package com.yusof.web.api.converter;

import com.yusof.web.entity.TransactionCategoryModel;
import com.yusof.web.service.TransactionCategoryDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TransactionCategoryModelToTransactionCategoryDtoConverter implements Converter<TransactionCategoryModel, TransactionCategoryDTO> {

    @Override
    public TransactionCategoryDTO convert(TransactionCategoryModel source) {
        return new TransactionCategoryDTO(
                source.getId(),
                source.getName(),
                source.getClientId());
    }
}
