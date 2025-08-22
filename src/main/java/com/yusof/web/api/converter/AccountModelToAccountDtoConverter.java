package com.yusof.web.api.converter;

import com.yusof.web.entity.AccountModel;
import com.yusof.web.service.AccountDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class AccountModelToAccountDtoConverter implements Converter<AccountModel, AccountDTO> {

    @Override
    public AccountDTO convert(AccountModel source) {
        return new AccountDTO(
                source.getId(),
                source.getName(),
                source.getBalance(),
                source.getClientId());
    }
}