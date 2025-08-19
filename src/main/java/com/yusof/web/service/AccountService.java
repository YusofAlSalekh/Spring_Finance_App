package com.yusof.web.service;

import com.yusof.web.entity.AccountModel;
import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.IllegalOwnerException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.exceptions.OperationFailedException;
import com.yusof.web.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final Converter<AccountModel, AccountDTO> accountDtoConverter;

    public List<AccountDTO> viewAccount(int clientId) {
        List<AccountModel> accountModels = accountRepository.findByClientId(clientId);
        List<AccountDTO> accountDTOs = new ArrayList<>();

        for (AccountModel accountModel : accountModels) {
            accountDTOs.add(accountDtoConverter.convert(accountModel));
        }
        return accountDTOs;
    }

    @Transactional
    public AccountDTO createAccount(String accountName, BigDecimal balance, int clientId) {
        boolean exists = accountRepository.existsByClientIdAndNameIgnoreCase(clientId, accountName);

        if (!exists) {
            AccountModel accountModel = new AccountModel();
            accountModel.setBalance(balance);
            accountModel.setClientId(clientId);
            accountModel.setName(accountName);

            accountRepository.save(accountModel);
            return accountDtoConverter.convert(accountModel);
        } else throw new AlreadyExistsException("Account with name " + accountName + " already exists");
    }

    @Transactional
    public void deleteAccount(int accountId, int clientId) {
        accountExistenceValidation(accountId, clientId);

        int modifiedCount = accountRepository.deleteByIdAndClientId(accountId, clientId);

        if (modifiedCount == 0) {
            throw new OperationFailedException("Error has occurred while deleting account");
        }
    }

    @Transactional
    public AccountDTO updateAccountName(String newName, int accountId, int clientId) {
        int count = accountRepository.countByNameAndClientIdAndIdNot(newName, clientId, accountId);

        if (count > 0) {
            throw new AlreadyExistsException("The account with this name already exists");
        }

        AccountModel accountModel = accountExistenceValidation(accountId, clientId);

        accountModel.setName(newName);
        return accountDtoConverter.convert(accountModel);
    }

    private AccountModel accountExistenceValidation(int accountId, int clientId) {
        AccountModel accountModel = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("No account found with Id: " + accountId));

        if (accountModel.getClientId() != clientId) {
            throw new IllegalOwnerException("Account with Id: " + accountId + " belongs to another client");
        }
        return accountModel;
    }
}