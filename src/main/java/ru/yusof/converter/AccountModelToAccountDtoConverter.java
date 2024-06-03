package ru.yusof.converter;

import ru.yusof.dao.AccountModel;
import ru.yusof.service.AccountDTO;

public class AccountModelToAccountDtoConverter<S, T> implements Converter<AccountModel, AccountDTO> {

    @Override
    public AccountDTO convert(AccountModel source) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(source.getId());
        accountDTO.setName(source.getName());
        accountDTO.setBalance(source.getBalance());
        accountDTO.setClient_id(source.getClient_id());
        return accountDTO;
    }
}