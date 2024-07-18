package ru.yusof.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yusof.exceptions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDaoTest {
    TransactionDao subj;
    LocalDate startDate = LocalDate.of(2024, 7, 1);
    LocalDate endDate = LocalDate.of(2024, 7, 19);

    @BeforeEach
    void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem");
        System.setProperty("jdbcUser", "postgres");
        System.setProperty("jdbcPassword", "");
        System.setProperty("liquibaseFile", "liquibase_transaction_dao_test.xml");

        subj = DaoFactory.getTransactionTypeDao();
    }

    @Test
    void createTransactionType_TransactionTypeWasNotCreated() {
        assertThrows(CreatingTransactionTypeException.class, () -> {
            subj.createTransactionType("school", 3);
        });
    }

    @Test
    void createTransactionType_TransactionTypeWasCreated() {
        TransactionTypeModel createdTransactionType = subj.createTransactionType("something", 1);

        assertNotNull(createdTransactionType);
        assertEquals("something", createdTransactionType.getName());
        assertEquals(1, createdTransactionType.getClientId());
    }

    @Test
    void deleteTransactionType_TransactionTypeWasNotDeleted() {
        assertThrows(DeletionTransactionTypeException.class, () -> {
            subj.deleteTransactionType(1, 100);
        });
    }

    @Test
    void deleteTransactionType_TransactionTypeWasDeleted() {
        boolean result = subj.deleteTransactionType(2, 1);

        assertTrue(result);

        assertThrows(DeletionTransactionTypeException.class, () -> {
            subj.deleteTransactionType(2, 1);
        });
    }

    @Test
    void editTransactionType_TransactionTypeWasNotEdited() {
        assertThrows(AddingTransactionTypeException.class, () -> {
            subj.editTransactionType("health", 1, 100);
        });
    }

    @Test
    void editTransactionType_TransactionTypeWasEdited() {
        boolean result = subj.editTransactionType("health", 2, 1);

        assertTrue(result);
    }

    @Test
    void fetchExpenseByCategory_WasFetched() {
        BigDecimal bigDecimalValue = new BigDecimal("10.00");
        List<Integer> categoryIds = new ArrayList<>();

        categoryIds.add(2);
        subj.addTransaction(2, 1, bigDecimalValue, categoryIds);

        List<CategoryAmountModel> result = subj.fetchExpenseByCategory(2, startDate, endDate);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(bigDecimalValue, result.get(0).getTotalAmount());
        assertEquals("education", result.get(0).getCategoryName());
    }

    @Test
    void fetchExpenseByCategory_WasNotFetched() {
        assertThrows(GetExpenseByCategoryException.class, () -> {
            subj.fetchExpenseByCategory(100, startDate, endDate);
        });
    }

    @Test
    void fetchIncomeByCategory_WasFetched() {
        BigDecimal bigDecimalValue = new BigDecimal("10.00");
        List<Integer> categoryIds = new ArrayList<>();

        categoryIds.add(2);
        subj.addTransaction(2, 1, bigDecimalValue, categoryIds);
        List<CategoryAmountModel> result = subj.fetchIncomeByCategory(1, startDate, endDate);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(bigDecimalValue, result.get(0).getTotalAmount());
        assertEquals("education", result.get(0).getCategoryName());
    }

    @Test
    void fetchIncomeByCategory_NoRecordsFound() {
        List<CategoryAmountModel> result = subj.fetchIncomeByCategory(1, startDate, endDate);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void fetchExpanseByCategory_NoRecordsFound() {
        List<CategoryAmountModel> result = subj.fetchExpenseByCategory(2, startDate, endDate);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void fetchIncomeByCategory_WasNotFetched() {
        assertThrows(GetIncomeByCategoryException.class, () -> {
            subj.fetchIncomeByCategory(100, startDate, endDate);
        });
    }

    @Test
    void clientIdExists() {
        boolean result = subj.clientIdExists(1);

        assertTrue(result);
    }

    @Test
    void clientIdDoesNotExist() {
        boolean result = subj.clientIdExists(3);

        assertFalse(result);
    }

    @Test
    void addTransaction_transactionWasAdded() {
        BigDecimal bigDecimalValue10 = new BigDecimal("10.00");
        BigDecimal bigDecimalValue20 = new BigDecimal("20.00");
        BigDecimal bigDecimalValue30 = new BigDecimal("30.00");
        List<Integer> categoryIds = new ArrayList<>();

        // Add initial category ID
        categoryIds.add(2);
        subj.addTransaction(2, 1, bigDecimalValue10, categoryIds);

        // Check expenses after first transaction
        List<CategoryAmountModel> expenseResults = subj.fetchExpenseByCategory(2, startDate, endDate);

        assertNotNull(expenseResults);
        assertFalse(expenseResults.isEmpty());
        assertEquals(1, expenseResults.size());
        assertEquals(bigDecimalValue10, expenseResults.get(0).getTotalAmount());
        assertNotNull(expenseResults);
        assertFalse(expenseResults.isEmpty());
        assertEquals(1, expenseResults.size());
        assertEquals("education", expenseResults.get(0).getCategoryName());

        // Add another category ID and perform second transaction
        categoryIds.add(3);
        subj.addTransaction(2, 1, bigDecimalValue20, categoryIds);

        // Check expenses after second transaction
        expenseResults = subj.fetchExpenseByCategory(2, startDate, endDate);

        assertNotNull(expenseResults);
        assertFalse(expenseResults.isEmpty());
        assertEquals(2, expenseResults.size());
        assertEquals(bigDecimalValue30, expenseResults.get(0).getTotalAmount());
        assertEquals("education", expenseResults.get(0).getCategoryName());
        assertEquals(bigDecimalValue20, expenseResults.get(1).getTotalAmount());
        assertEquals("health", expenseResults.get(1).getCategoryName());
    }

    @Test
    void addTransaction_transactionWasNotAdded() {
        BigDecimal amount = new BigDecimal("10000.00");
        List<Integer> categoryIds = new ArrayList<>();

        categoryIds.add(2);
        assertThrows(InsufficientFundsException.class,
                () -> {
                    subj.addTransaction(2, 1, amount, categoryIds);
                });
    }
}