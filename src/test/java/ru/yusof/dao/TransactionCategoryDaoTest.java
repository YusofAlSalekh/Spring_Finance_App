package ru.yusof.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yusof.exceptions.AddingTransactionCategoryException;
import ru.yusof.exceptions.DeletionTransactionCategoryException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCategoryDaoTest {
    TransactionCategoryDao subj;

    @BeforeEach
    void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem" + UUID.randomUUID().toString());
        System.setProperty("jdbcUser", "postgres");
        System.setProperty("jdbcPassword", "");
        System.setProperty("liquibaseFile", "liquibase_transaction_category_dao_test.xml");

        subj = DaoFactory.getTransactionCategoryDao();
    }

    @AfterEach
    public void after() {
        DaoFactory.resetDataSource();
        DaoFactory.resetDao();
    }

    @Test
    void createTransactionType_transactionTypeWasCreated() {
        TransactionCategoryModel createdTransactionType = subj.createTransactionCategory("something", 1);

        assertNotNull(createdTransactionType);
        assertEquals("something", createdTransactionType.getName());
        assertEquals(1, createdTransactionType.getClientId());
    }

    @Test
    void deleteTransactionType_transactionTypeWasNotDeleted() {
        assertThrows(DeletionTransactionCategoryException.class, () -> {
            subj.deleteTransactionCategory(1, 100);
        });
    }

    @Test
    void deleteTransactionType_transactionTypeWasDeleted() {
        boolean result = subj.deleteTransactionCategory(2, 1);

        assertTrue(result);

        assertThrows(DeletionTransactionCategoryException.class, () -> {
            subj.deleteTransactionCategory(2, 1);
        });
    }

    @Test
    void editTransactionType_transactionTypeWasNotEdited() {
        assertThrows(AddingTransactionCategoryException.class, () -> {
            subj.editTransactionCategory("health", 1, 100);
        });
    }

    @Test
    void editTransactionType_transactionTypeWasEdited() {
        boolean result = subj.editTransactionCategory("health", 2, 1);

        assertTrue(result);
    }
}