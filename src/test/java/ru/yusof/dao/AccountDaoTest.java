package ru.yusof.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.yusof.exceptions.DeletionAccountException;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountDaoTest {
    AccountDao subj;
    ApplicationContext context;

    @BeforeEach
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem" + UUID.randomUUID().toString());
        System.setProperty("jdbcUser", "postgres");
        System.setProperty("jdbcPassword", "");
        System.setProperty("liquibaseFile", "liquibase_account_dao_test.xml");

        context = new AnnotationConfigApplicationContext("ru.yusof");
        subj = context.getBean(AccountDao.class);
    }

    @Test
    void findByClientID_accountExists() {
        subj.createAccount("yusof", new BigDecimal("100"), 1);
        List<AccountModel> accountModels = subj.findByClientID(1);
        assertFalse(accountModels.isEmpty(), "Should find at least one account");
        assertEquals("yusof", accountModels.get(0).getName());
        assertEquals(1, accountModels.get(0).getClientId());
        assertEquals(new BigDecimal("100"), accountModels.get(0).getBalance());
    }

    @Test
    void findByClientID_accountDoesNotExists() {
        assertThrows(NoSuchElementException.class, () -> {
            subj.findByClientID(2);
        }, "No accounts found for client ID");
    }

    @Test
    void createAccount_accountWasCreated() {
        AccountModel createdAccount = subj.createAccount("yusof", new BigDecimal("100.00"), 1);

        assertNotNull(createdAccount);
        assertEquals("yusof", createdAccount.getName());
        assertEquals(new BigDecimal("100.00"), createdAccount.getBalance());
        assertEquals(1, createdAccount.getClientId());
    }

    @Test
    void deleteAccount_accountWasDeleted() {
        subj.createAccount("ganik", new BigDecimal("100.00"), 1);
        subj.deleteAccount(1, 1);

        assertThrows(NoSuchElementException.class, () -> {
            subj.findByClientID(1);
        }, "No accounts found for client ID");
    }

    @Test
    void deleteAccount_accountWasNotDeleted() {
        int nonExistentAccountId = 10;
        int clientId = 1;

        assertThrows(DeletionAccountException.class, () -> {
            subj.deleteAccount(nonExistentAccountId, clientId);
        }, "No account found with ID: " + nonExistentAccountId + " for client ID: " + clientId);
    }
}