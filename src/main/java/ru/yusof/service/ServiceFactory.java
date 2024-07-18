package ru.yusof.service;

import ru.yusof.dao.DaoFactory;

import static ru.yusof.converter.ConverterFactory.*;
import static ru.yusof.dao.DaoFactory.getUserDao;

public class ServiceFactory {
    private static AuthorizationService authorizationService;
    private static AccountService accountService;
    private static TransactionService transactionService;
    private static DigestService digestService;

    public static AuthorizationService getAuthorizationService() {
        if (authorizationService == null) {
            authorizationService = new AuthorizationService(
                    getUserDao(),
                    getDigestService(),
                    getUserModelToUserDtoConverter()
            );
        }
        return authorizationService;
    }

    public static DigestService getDigestService() {
        if (digestService == null) {
            digestService = new Md5DigestService();
        }
        return digestService;
    }

    public static AccountService getAccountService() {
        if (accountService == null) {
            accountService = new AccountService(
                    DaoFactory.getAccountDao(),
                    getAccountModelToAccountDtoConverter()
            );
        }
        return accountService;
    }

    public static TransactionService getTransactionTypeService() {
        if (transactionService == null) {
            transactionService = new TransactionService(
                    DaoFactory.getTransactionTypeDao(),
                    getTransactionTypeModelToTransactionDtoConverter()
            );
        }
        return transactionService;
    }
}