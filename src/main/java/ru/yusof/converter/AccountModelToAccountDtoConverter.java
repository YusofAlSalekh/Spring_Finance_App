package ru.yusof.converter;

import org.springframework.stereotype.Service;
import ru.yusof.dao.AccountModel;
import ru.yusof.service.AccountDTO;

@Service
public class AccountModelToAccountDtoConverter<S, T> implements Converter<AccountModel, AccountDTO> {

    @Override
    public AccountDTO convert(AccountModel source) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(source.getId());
        accountDTO.setName(source.getName());
        accountDTO.setBalance(source.getBalance());
        accountDTO.setClientId(source.getClientId());
        return accountDTO;
    }
}