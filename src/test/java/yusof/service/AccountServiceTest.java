package yusof.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yusof.converter.AccountModelToAccountDtoConverter;
import yusof.dao.AccountDao;
import yusof.dao.AccountModel;
import yusof.exceptions.CustomException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @InjectMocks
    AccountService subj;

    @Mock
    AccountDao accountDao;

    @Mock
    AccountModelToAccountDtoConverter accountDtoConverter;

    @Test
    void viewAccount() {
        AccountModel accountModel = new AccountModel();
        accountModel.setId(1);
        accountModel.setBalance(100);
        accountModel.setName("yusof");
        accountModel.setClient_id(1);
        List<AccountModel> accountModels = Arrays.asList(accountModel);
        when(accountDao.findByClientID(1)).thenReturn(accountModels);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance(100);
        accountDTO.setClient_id(1);
        accountDTO.setId(1);
        accountDTO.setName("yusof");
        when(accountDtoConverter.convert(accountModel)).thenReturn(accountDTO);

        List<AccountDTO> accountDTOS = subj.viewAccount(1);

        assertNotNull(accountDTOS);
        assertEquals(accountDTO, accountDTOS.get(0));

        verify(accountDao, times(1)).findByClientID(1);
        verify(accountDtoConverter, times(1)).convert(accountModel);
    }

    @Test
    void viewAccountDoesntWork() {

        when(accountDao.findByClientID(1)).thenThrow(new CustomException("Viewing failure"));

        assertThrows(CustomException.class, () -> subj.viewAccount(1), "Viewing failure");

        verify(accountDao, times(1)).findByClientID(1);
        verifyNoInteractions(accountDtoConverter);
    }

    @Test
    void accountCreated() {
        AccountModel accountModel = new AccountModel();
        accountModel.setId(10);
        accountModel.setBalance(100);
        accountModel.setName("yusoff");
        accountModel.setClient_id(10);
        when(accountDao.createAccount("yusoff", 100, 10)).thenReturn(accountModel);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(10);
        accountDTO.setBalance(100);
        accountDTO.setName("yusoff");
        accountDTO.setClient_id(10);
        when(accountDtoConverter.convert(accountModel)).thenReturn(accountDTO);

        AccountDTO account = subj.createAccount("yusoff", 100, 10);
        assertNotNull(account);
        assertEquals(account, accountDTO);

        verify(accountDtoConverter, times(1)).convert(accountModel);
        verify(accountDao, times(1)).createAccount("yusoff", 100, 10);
    }

    @Test
    void accountWasNotCreatedSomethingWentWrong() {
        when(accountDao.createAccount("yusoff", 100, 10)).thenThrow(new CustomException("Something went wrong during creating account. Please, try again later"));

        assertThrows(CustomException.class, () ->
                        subj.createAccount("yusoff", 100, 10)
                , "Something went wrong during creating account. Please, try again later");


        verify(accountDao, times(1)).createAccount("yusoff", 100, 10);
        verifyNoInteractions(accountDtoConverter);
    }

    @Test
    void deleteAccountWorked() {
        subj.deleteAccount(1, 1);

        verify(accountDao, times(1)).deleteAccount(1, 1);
    }

    @Test
    void deleteAccountFailed() {
        doThrow(new CustomException("Deletion failed")).when(accountDao).deleteAccount(1, 1);

        assertThrows(CustomException.class, () -> subj.deleteAccount(1, 1), "Deletion failed");
        verify(accountDao, times(1)).deleteAccount(1, 1);
    }
}