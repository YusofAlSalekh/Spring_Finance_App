package ru.yusof.converter;

import ru.yusof.dao.AccountModel;
import ru.yusof.dao.TransactionTypeModel;
import ru.yusof.dao.UserModel;
import ru.yusof.service.AccountDTO;
import ru.yusof.service.TransactionDTO;
import ru.yusof.service.UserDTO;

public class ConverterFactory {
   public static Converter<UserModel, UserDTO> userModelToUserDtoConverter;
   public static Converter<TransactionTypeModel, TransactionDTO> transactionTypeModelToTransactionDtoConverter;
   public static Converter<AccountModel, AccountDTO> accountModelToAccountDtoConverter;

   public static Converter<TransactionTypeModel, TransactionDTO> getTransactionTypeModelToTransactionDtoConverter(){
       if(transactionTypeModelToTransactionDtoConverter == null){
           transactionTypeModelToTransactionDtoConverter = new TransactionTypeModelToTransactionDtoConverter<>();
       }
       return transactionTypeModelToTransactionDtoConverter;
   }

    public static Converter<AccountModel, AccountDTO> getAccountModelToAccountDtoConverter(){
        if(accountModelToAccountDtoConverter == null){
            accountModelToAccountDtoConverter = new AccountModelToAccountDtoConverter<>();
        }
        return accountModelToAccountDtoConverter;
    }

    public static Converter<UserModel, UserDTO> getUserModelToUserDtoConverter(){
        if(userModelToUserDtoConverter == null){
            userModelToUserDtoConverter = new UserModelToUserDtoConverter<>();
        }
        return userModelToUserDtoConverter;
    }
}