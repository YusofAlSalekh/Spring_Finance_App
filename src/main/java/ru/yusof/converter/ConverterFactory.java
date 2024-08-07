package ru.yusof.converter;

import ru.yusof.dao.AccountModel;
import ru.yusof.dao.TransactionCategoryModel;
import ru.yusof.dao.TransactionModel;
import ru.yusof.dao.UserModel;
import ru.yusof.service.AccountDTO;
import ru.yusof.service.TransactionCategoryDTO;
import ru.yusof.service.TransactionDTO;
import ru.yusof.service.UserDTO;

public class ConverterFactory {
    public static Converter<UserModel, UserDTO> userModelToUserDtoConverter;
    public static Converter<TransactionCategoryModel, TransactionCategoryDTO> transactionCategoryModelToTransactionCategoryDtoConverter;
    public static Converter<TransactionModel, TransactionDTO> transactionModelToTransactionDtoConverter;
    public static Converter<AccountModel, AccountDTO> accountModelToAccountDtoConverter;

    public static Converter<TransactionCategoryModel, TransactionCategoryDTO> getTransactionCategoryModelToTransactionCategoryDtoConverter() {
        if (transactionCategoryModelToTransactionCategoryDtoConverter == null) {
            transactionCategoryModelToTransactionCategoryDtoConverter = new TransactionCategoryModelToTransactionCategoryDtoConverter<>();
        }
        return transactionCategoryModelToTransactionCategoryDtoConverter;
    }

    public static Converter<TransactionModel, TransactionDTO> getTransactionModelToTransactionDtoConverter() {
        if (transactionModelToTransactionDtoConverter == null) {
            transactionModelToTransactionDtoConverter = new TransactionModelToTransactionDtoConverter<>();
        }
        return transactionModelToTransactionDtoConverter;
    }

    public static Converter<AccountModel, AccountDTO> getAccountModelToAccountDtoConverter() {
        if (accountModelToAccountDtoConverter == null) {
            accountModelToAccountDtoConverter = new AccountModelToAccountDtoConverter<>();
        }
        return accountModelToAccountDtoConverter;
    }

    public static Converter<UserModel, UserDTO> getUserModelToUserDtoConverter() {
        if (userModelToUserDtoConverter == null) {
            userModelToUserDtoConverter = new UserModelToUserDtoConverter<>();
        }
        return userModelToUserDtoConverter;
    }
}