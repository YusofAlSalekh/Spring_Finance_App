package yusof.service;

import yusof.converter.AccountModelToAccountDtoConverter;
import yusof.converter.TransactionTypeModelToTransactionDtoConverter;
import yusof.converter.UserModelToUserDtoConverter;
import yusof.dao.AccountDao;
import yusof.dao.TransactionTypeDao;
import yusof.dao.UserDao;

public class ServiceFactory {
    private static AuthorizationService authorizationService;
    private static AccountService accountService;

    private static TransactionTypeService transactionTypeService;

    public static AuthorizationService getAuthorizationService() {
        if (authorizationService == null) {
            authorizationService = new AuthorizationService(
                    new UserDao(),
                    new Md5DigestService(),
                    new UserModelToUserDtoConverter()
            );
        }
        return authorizationService;
    }

    public static AccountService getAccountService() {
        if (accountService == null) {
            accountService = new AccountService(
                    new AccountDao(),
                    new AccountModelToAccountDtoConverter()
            );
        }
        return accountService;
    }

    public static TransactionTypeService getTransactionTypeService() {
        if (transactionTypeService == null) {
            transactionTypeService = new TransactionTypeService(
                    new TransactionTypeDao(),
                    new TransactionTypeModelToTransactionDtoConverter()
            );
        }
        return transactionTypeService;
    }
}
