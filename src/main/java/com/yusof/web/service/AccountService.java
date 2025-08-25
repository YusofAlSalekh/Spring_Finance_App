package com.yusof.web.service;

import com.yusof.web.entity.AccountModel;
import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.repository.AccountRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Validated
public class AccountService {
    private final AccountRepository accountRepository;
    private final Converter<AccountModel, AccountDTO> accountDtoConverter;

    public List<AccountDTO> viewAccount(int clientId) {
        List<AccountModel> accountModels = accountRepository.findByClientId(clientId);

        return accountModels.stream()
                .map(accountDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccountDTO createAccount(@NotBlank String accountName, @NotNull @PositiveOrZero BigDecimal balance, int clientId) {
        ensureNameUniqueForCreation(accountName, clientId);
        AccountModel accountModel = getAccountModel(accountName, balance, clientId);
        accountRepository.save(accountModel);

        return accountDtoConverter.convert(accountModel);
    }

    @Transactional
    public void deleteAccount(@NotNull @Positive int accountId, int clientId) {
        assertAccountExists(accountId, clientId);

        accountRepository.deleteByIdAndClientId(accountId, clientId);
    }

    @Transactional
    public AccountDTO updateAccountName(@NotBlank String newName, @NotNull @Positive int accountId, int clientId) {
        ensureNameUniqueForUpdate(newName, accountId, clientId);

        AccountModel accountModel = assertAccountExists(accountId, clientId);

        accountModel.setName(newName);
        return accountDtoConverter.convert(accountModel);
    }

    private void ensureNameUniqueForUpdate(String newName, int accountId, int clientId) {
        int count = accountRepository.countByNameAndClientIdAndIdNot(newName, clientId, accountId);

        if (count > 0) {
            throw new AlreadyExistsException("The account with this name already exists");
        }
    }

    private AccountModel assertAccountExists(int accountId, int clientId) {
        return accountRepository.findByIdAndClientId(accountId, clientId)
                .orElseThrow(() -> new NotFoundException("No account found with Id: " + accountId));
    }

    private void ensureNameUniqueForCreation(String accountName, int clientId) {
        if (accountRepository.existsByClientIdAndNameIgnoreCase(clientId, accountName)) {
            throw new AlreadyExistsException("Account with name " + accountName + " already exists");
        }
    }

    private static AccountModel getAccountModel(String accountName, BigDecimal balance, int clientId) {
        AccountModel accountModel = new AccountModel();
        accountModel.setBalance(balance);
        accountModel.setClientId(clientId);
        accountModel.setName(accountName);
        return accountModel;
    }
}