package com.yusof.web.service;

import com.yusof.web.entity.AccountModel;
import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.IllegalOwnerException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.exceptions.OperationFailedException;
import com.yusof.web.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @InjectMocks
    AccountService subject;

    @Mock
    AccountRepository accountRepository;

    @Mock
    Converter<AccountModel, AccountDTO> accountDtoConverter;

    @Test
    void viewAccount_success() {
        int clientId = 1;

        AccountModel model1 = new AccountModel();
        model1.setId(1);
        model1.setName("Account1");
        model1.setBalance(new BigDecimal("100.00"));
        model1.setClientId(clientId);

        AccountModel model2 = new AccountModel();
        model2.setId(2);
        model2.setName("Account2");
        model2.setBalance(new BigDecimal("200.00"));
        model2.setClientId(clientId);

        List<AccountModel> models = List.of(model1, model2);
        when(accountRepository.findByClientId(clientId)).thenReturn(models);

        AccountDTO dto1 = new AccountDTO(1, "Account1", new BigDecimal("100.00"), clientId);
        AccountDTO dto2 = new AccountDTO(2, "Account2", new BigDecimal("200.00"), clientId);

        when(accountDtoConverter.convert(model1)).thenReturn(dto1);
        when(accountDtoConverter.convert(model2)).thenReturn(dto2);

        List<AccountDTO> result = subject.viewAccount(clientId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));

        verify(accountRepository).findByClientId(clientId);
        verify(accountDtoConverter).convert(model1);
        verify(accountDtoConverter).convert(model2);
        verifyNoMoreInteractions(accountRepository, accountDtoConverter);
    }

    @Test
    void viewAccount_returnsEmptyList() {
        int clientId = 1;

        when(accountRepository.findByClientId(clientId)).thenReturn(Collections.emptyList());

        List<AccountDTO> result = subject.viewAccount(clientId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(accountRepository).findByClientId(clientId);
        verifyNoMoreInteractions(accountRepository, accountDtoConverter);
    }

    @Test
    void createAccount_success() {
        String name = "AccountName";
        BigDecimal balance = new BigDecimal("100.00");
        int clientId = 1;

        when(accountRepository.existsByClientIdAndNameIgnoreCase(clientId, name)).thenReturn(false);

        AccountDTO expectedDTO = new AccountDTO();
        expectedDTO.setId(1);
        expectedDTO.setName(name);
        expectedDTO.setBalance(balance);
        expectedDTO.setClientId(clientId);
        when(accountDtoConverter.convert(any(AccountModel.class))).thenReturn(expectedDTO);

        AccountDTO result = subject.createAccount(name, balance, clientId);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(name, result.getName());
        assertEquals(balance, result.getBalance());
        assertEquals(clientId, result.getClientId());

        verify(accountRepository).existsByClientIdAndNameIgnoreCase(clientId, name);
        verify(accountRepository).save(any(AccountModel.class));
        verify(accountDtoConverter).convert(any(AccountModel.class));
        verifyNoMoreInteractions(accountRepository, accountDtoConverter);
    }

    @Test
    void createAccount_throwsAlreadyExistsException() {
        String name = "AccountName";
        BigDecimal balance = new BigDecimal("100.00");
        int clientId = 1;

        when(accountRepository.existsByClientIdAndNameIgnoreCase(clientId, name)).thenReturn(true);

        AlreadyExistsException ex = assertThrowsExactly(
                AlreadyExistsException.class,
                () -> subject.createAccount(name, balance, clientId)
        );

        assertTrue(ex.getMessage().contains("Account with name " + name + " already exists"));

        verify(accountRepository).existsByClientIdAndNameIgnoreCase(clientId, name);
        verify(accountRepository, never()).save(any());
        verify(accountDtoConverter, never()).convert(any());
    }

    @Test
    void deleteAccount_success() {
        int accountId = 1;
        int clientId = 1;

        AccountModel accountModel = new AccountModel();
        accountModel.setId(accountId);
        accountModel.setClientId(clientId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountModel));

        when(accountRepository.deleteByIdAndClientId(accountId, clientId)).thenReturn(1);

        assertDoesNotThrow(() -> subject.deleteAccount(accountId, clientId));

        verify(accountRepository).findById(accountId);
        verify(accountRepository).deleteByIdAndClientId(accountId, clientId);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void deleteAccount_throwsOperationFailedException() {
        int accountId = 1;
        int clientId = 1;

        AccountModel model = new AccountModel();
        model.setId(accountId);
        model.setClientId(clientId);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(model));

        when(accountRepository.deleteByIdAndClientId(accountId, clientId)).thenReturn(0);

        OperationFailedException ex = assertThrowsExactly(
                OperationFailedException.class,
                () -> subject.deleteAccount(accountId, clientId)
        );

        assertTrue(ex.getMessage().contains("Error has occurred while deleting account"));

        verify(accountRepository).findById(accountId);
        verify(accountRepository).deleteByIdAndClientId(accountId, clientId);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void deleteAccount_throwsNotFoundException() {
        int accountId = 1;
        int clientId = 1;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrowsExactly(NotFoundException.class,
                () -> subject.deleteAccount(accountId, clientId));

        verify(accountRepository).findById(accountId);
        verify(accountRepository, never()).deleteByIdAndClientId(anyInt(), anyInt());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void deleteAccount_whenIllegalOwner_throwsIllegalOwnerException() {
        int accountId = 1;
        int clientId = 1;

        AccountModel accountModel = new AccountModel();
        accountModel.setId(accountId);
        accountModel.setClientId(2);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountModel));

        assertThrowsExactly(IllegalOwnerException.class,
                () -> subject.deleteAccount(accountId, clientId));

        verify(accountRepository).findById(accountId);
        verify(accountRepository, never()).deleteByIdAndClientId(anyInt(), anyInt());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void updateAccountName_success() {
        String newName = "NewName";
        String oldName = "OldName";
        int accountId = 1;
        int clientId = 1;
        BigDecimal balance = new BigDecimal("100.00");

        when(accountRepository.countByNameAndClientIdAndIdNot(newName, clientId, accountId)).thenReturn(0);

        AccountModel accountModel = new AccountModel();
        accountModel.setId(accountId);
        accountModel.setClientId(clientId);
        accountModel.setName(oldName);
        accountModel.setBalance(balance);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountModel));

        AccountDTO expected = new AccountDTO();
        expected.setId(accountId);
        expected.setClientId(clientId);
        expected.setName(newName);
        expected.setBalance(balance);
        when(accountDtoConverter.convert(any(AccountModel.class))).thenReturn(expected);

        AccountDTO result = subject.updateAccountName(newName, accountId, clientId);

        assertNotNull(result);
        assertEquals(accountId, result.getId());
        assertEquals(clientId, result.getClientId());
        assertEquals(newName, result.getName());
        assertEquals(balance, result.getBalance());

        verify(accountRepository).countByNameAndClientIdAndIdNot(newName, clientId, accountId);
        verify(accountRepository).findById(accountId);
        verify(accountDtoConverter).convert(argThat(m -> m == accountModel && m.getName().equals(newName)));
        verifyNoMoreInteractions(accountRepository, accountDtoConverter);
    }

    @Test
    void updateAccountName_throwsAlreadyExistsException() {
        String newName = "NewName";
        int accountId = 1;
        int clientId = 1;

        when(accountRepository.countByNameAndClientIdAndIdNot(newName, clientId, accountId)).thenReturn(1);

        AlreadyExistsException ex = assertThrowsExactly(
                AlreadyExistsException.class,
                () -> subject.updateAccountName(newName, accountId, clientId)
        );

        assertTrue(ex.getMessage().contains("The account with this name already exists"));

        verify(accountRepository).countByNameAndClientIdAndIdNot(newName, clientId, accountId);
        verifyNoMoreInteractions(accountRepository);
        verify(accountDtoConverter, never()).convert(any());
    }

    @Test
    void updateAccountName_throwsNotFoundException() {
        String newName = "NewName";
        int accountId = 1;
        int clientId = 1;

        when(accountRepository.countByNameAndClientIdAndIdNot(newName, clientId, accountId)).thenReturn(0);
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrowsExactly(
                NotFoundException.class,
                () -> subject.updateAccountName(newName, accountId, clientId)
        );
        assertTrue(ex.getMessage().contains("No account found with Id: " + accountId));

        verify(accountRepository).countByNameAndClientIdAndIdNot(newName, clientId, accountId);
        verify(accountRepository).findById(accountId);
        verifyNoMoreInteractions(accountRepository);
        verify(accountDtoConverter, never()).convert(any());
    }

    @Test
    void updateAccountName_throwsIllegalOwnerException() {
        String newName = "NewName";
        int accountId = 1;
        int clientId = 1;

        when(accountRepository.countByNameAndClientIdAndIdNot(newName, clientId, accountId)).thenReturn(0);

        AccountModel accountModel = new AccountModel();
        accountModel.setId(accountId);
        accountModel.setClientId(2);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountModel));

        IllegalOwnerException ex = assertThrowsExactly(
                IllegalOwnerException.class,
                () -> subject.updateAccountName(newName, accountId, clientId)
        );
        assertTrue(ex.getMessage().contains("Account with Id: " + accountId + " belongs to another client"));

        verify(accountRepository).countByNameAndClientIdAndIdNot(newName, clientId, accountId);
        verify(accountRepository).findById(accountId);
        verifyNoMoreInteractions(accountRepository);
        verify(accountDtoConverter, never()).convert(any());
    }
}
