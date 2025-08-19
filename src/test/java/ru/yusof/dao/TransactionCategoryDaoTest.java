package ru.yusof.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.yusof.entity.TransactionCategoryModel;
import ru.yusof.exceptions.AlreadyExistsException;
import ru.yusof.exceptions.NotFoundException;
import ru.yusof.exceptions.OperationFailedException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCategoryDaoTest {
    TransactionCategoryDao subj;
    ApplicationContext context;

    @BeforeEach
    void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem" + UUID.randomUUID().toString());
        System.setProperty("jdbcUser", "postgres");
        System.setProperty("jdbcPassword", "");
        System.setProperty("liquibaseFile", "liquibase_transaction_category_dao_test.xml");

        context = new AnnotationConfigApplicationContext("ru.yusof");
        subj = context.getBean(TransactionCategoryDao.class);
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
        assertThrows(NotFoundException.class, () -> {
            subj.deleteTransactionCategory(1, 100);
        });
    }

    @Test
    void deleteTransactionType_transactionTypeWasDeleted() {
        boolean result = subj.deleteTransactionCategory(2, 1);

        assertTrue(result);
        assertThrows(NotFoundException.class, () -> {
            subj.deleteTransactionCategory(2, 1);
        });
    }

    @Test
    void editTransactionType_transactionTypeWasNotEdited() {
        assertThrows(NotFoundException.class, () -> {
            subj.updateTransactionCategoryName("health", 1, 100);
        });
    }

    @Test
    void editTransactionType_transactionTypeWasEdited() {
        boolean result = subj.updateTransactionCategoryName("health", 2, 1);

        assertTrue(result);
    }
}