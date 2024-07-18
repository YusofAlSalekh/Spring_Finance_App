package ru.yusof.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yusof.exceptions.CreatingAccountException;
import ru.yusof.exceptions.DeletionAccountException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class AccountDaoTest {
    AccountDao subj;

    @BeforeEach
    void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem");
        System.setProperty("jdbcUser", "postgres");
        System.setProperty("jdbcPassword", "");
        System.setProperty("liquibaseFile", "liquibase_account_dao_test.xml");

        subj = DaoFactory.getAccountDao();
    }

    @Test
    void findByClientID_AccountExists() {
        subj.createAccount("yusof", 100, 1);
        List<AccountModel> accountModels = subj.findByClientID(1);
        assertFalse(accountModels.isEmpty(), "Should find at least one account");
        assertEquals("yusof", accountModels.get(0).getName());
        assertEquals(1, accountModels.get(0).getClient_id());
        assertEquals(100, accountModels.get(0).getBalance());
    }

    @Test
    void findByClientID_AccountDoesNotExists() {
        assertThrows(NoSuchElementException.class, () -> {
            subj.findByClientID(2);
        }, "No accounts found for client ID");
    }

    @Test
    void createAccount_AccountWasCreated() {
        AccountModel createdAccount = subj.createAccount("yusof", 100, 1);

        assertNotNull(createdAccount);
        assertEquals("yusof", createdAccount.getName());
        assertEquals(100, createdAccount.getBalance());
        assertEquals(1, createdAccount.getClient_id());
    }

    @Test
    void createAccount_AccountWasNotCreated() {
        assertThrows(CreatingAccountException.class, () -> {
            subj.createAccount("yusof", 100, 10);
        }, "Something went wrong during creating account. Please, try again later");
    }

    @Test
    void deleteAccount_AccountWasDeleted() {
        subj.createAccount("ganik", 10, 1);
        subj.deleteAccount(1, 1);

        assertThrows(NoSuchElementException.class, () -> {
            subj.findByClientID(1);
        }, "No accounts found for client ID");
    }

    @Test
    void deleteAccount_AccountWasNotDeleted() {
        int nonExistentAccountId = 10;
        int clientId = 1;

        assertThrows(DeletionAccountException.class, () -> {
            subj.deleteAccount(nonExistentAccountId, clientId);
        }, "No account found with ID: " + nonExistentAccountId + " for client ID: " + clientId);
    }
}