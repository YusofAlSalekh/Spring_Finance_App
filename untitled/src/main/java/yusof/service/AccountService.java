package yusof.service;

import yusof.converter.AccountModelToAccountDtoConverter;
import yusof.dao.AccountDao;
import yusof.dao.AccountModel;

import java.util.ArrayList;
import java.util.List;

public class AccountService {
    private final AccountDao accountDao;
    private final AccountModelToAccountDtoConverter accountDtoConverter;

    public AccountService() {
        this.accountDao = new AccountDao();
        this.accountDtoConverter = new AccountModelToAccountDtoConverter();
    }

    public List<AccountDTO> viewAccount(int clientId) {
        List<AccountModel> accountModels = accountDao.findByClientID(clientId);
        List<AccountDTO> accountDTOs = new ArrayList<>();

        for (AccountModel accountModel : accountModels) {
            accountDTOs.add(accountDtoConverter.convert(accountModel));
        }
        return accountDTOs;
    }

    public AccountDTO createAccount(String accountName, int balance, int clientId) {
        AccountModel accountModel = accountDao.createAccount(accountName, balance, clientId);

        if (accountModel == null) {
            return null;
        } else {
            return accountDtoConverter.convert(accountModel);
        }
    }

    public void deleteAccount(int accountId, int clientId) {
        accountDao.deleteAccount(accountId, clientId);
    }
}
