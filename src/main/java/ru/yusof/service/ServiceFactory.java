package ru.yusof.service;

import ru.yusof.converter.AccountModelToAccountDtoConverter;
import ru.yusof.converter.TransactionTypeModelToTransactionDtoConverter;
import ru.yusof.converter.UserModelToUserDtoConverter;
import ru.yusof.dao.DaoFactory;

public class ServiceFactory {
    private static AuthorizationService authorizationService;
    private static AccountService accountService;
    private static TransactionTypeService transactionTypeService;


    public static AuthorizationService getAuthorizationService() {
        if (authorizationService == null) {
            authorizationService = new AuthorizationService(
                    DaoFactory.getUserDao(),
                    new Md5DigestService(),
                    new UserModelToUserDtoConverter()
            );
        }
        return authorizationService;
    }

    public static AccountService getAccountService() {
        if (accountService == null) {
            accountService = new AccountService(
                    DaoFactory.getAccountDao(),
                    new AccountModelToAccountDtoConverter()
            );
        }
        return accountService;
    }

    public static TransactionTypeService getTransactionTypeService() {
        if (transactionTypeService == null) {
            transactionTypeService = new TransactionTypeService(
                    DaoFactory.getTransactionTypeDao(),
                    new TransactionTypeModelToTransactionDtoConverter()
            );
        }
        return transactionTypeService;
    }
}