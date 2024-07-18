package ru.yusof.service;

import ru.yusof.converter.Converter;
import ru.yusof.dao.AccountDao;
import ru.yusof.dao.AccountModel;

import java.util.ArrayList;
import java.util.List;

public class AccountService {
    private final AccountDao accountDao;
    private final Converter<AccountModel, AccountDTO> accountDtoConverter;

    public AccountService(AccountDao accountDao, Converter<AccountModel, AccountDTO> accountDtoConverter) {
        this.accountDao = accountDao;
        this.accountDtoConverter = accountDtoConverter;
    }

    public List<AccountDTO> viewAccount(int clientId) {
        List<AccountModel> accountModels = accountDao.findByClientID(clientId);
        List<AccountDTO> accountDTOs = new ArrayList<>();

        for (AccountModel accountModel : accountModels) {
            accountDTOs.add(accountDtoConverter.convert(accountModel));
        }
        return accountDTOs;
    }

    public AccountDTO createAccount(String accountName, double balance, int clientId) {
        AccountModel accountModel = accountDao.createAccount(accountName, balance, clientId);
        return accountDtoConverter.convert(accountModel);
    }

    public void deleteAccount(int accountId, int clientId) {
        accountDao.deleteAccount(accountId, clientId);
    }
}