package ru.yusof.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.yusof.exceptions.GetExpenseByCategoryException;
import ru.yusof.exceptions.GetIncomeByCategoryException;
import ru.yusof.exceptions.InsufficientFundsException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDaoTest {
    TransactionDao subj;
    ApplicationContext context;
    LocalDate startDate = LocalDate.of(2024, 7, 1);
    LocalDate endDate = LocalDate.of(2025, 7, 1);

    @BeforeEach
    void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem" + UUID.randomUUID().toString());
        System.setProperty("jdbcUser", "postgres");
        System.setProperty("jdbcPassword", "");
        System.setProperty("liquibaseFile", "liquibase_transaction_dao_test.xml");

        context = new AnnotationConfigApplicationContext("ru.yusof");
        subj = context.getBean(TransactionDao.class);
    }

    @Test
    void fetchExpenseByCategory_wasFetched() {
        BigDecimal bigDecimalValue = new BigDecimal("10.00");
        List<Integer> categoryIds = new ArrayList<>();

        categoryIds.add(2);
        subj.addTransaction(2, 1, 2, bigDecimalValue, categoryIds);

        List<CategoryAmountModel> result = subj.fetchExpenseByCategory(2, startDate, endDate);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(bigDecimalValue, result.get(0).getTotalAmount());
        assertEquals("education", result.get(0).getCategoryName());
    }

    @Test
    void fetchExpenseByCategory_wasNotFetched() {
        assertThrows(GetExpenseByCategoryException.class, () -> {
            subj.fetchExpenseByCategory(1, startDate, endDate);
        });
    }

    @Test
    void fetchIncomeByCategory_wasFetched() {
        BigDecimal bigDecimalValue = new BigDecimal("10.00");
        List<Integer> categoryIds = new ArrayList<>();

        categoryIds.add(2);
        subj.addTransaction(2, 1, 2, bigDecimalValue, categoryIds);
        List<CategoryAmountModel> result = subj.fetchIncomeByCategory(1, startDate, endDate);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(bigDecimalValue, result.get(0).getTotalAmount());
        assertEquals("education", result.get(0).getCategoryName());
    }

    @Test
    void fetchIncomeByCategory_wasNotFetched() {
        assertThrows(GetIncomeByCategoryException.class, () -> {
            subj.fetchIncomeByCategory(1, startDate, endDate);
        });
    }

    @Test
    void addTransaction_transactionWasAdded() {
        BigDecimal bigDecimalValue10 = new BigDecimal("10.00");
        BigDecimal bigDecimalValue20 = new BigDecimal("20.00");
        BigDecimal bigDecimalValue30 = new BigDecimal("30.00");
        List<Integer> categoryIds = new ArrayList<>();

        categoryIds.add(2);
        subj.addTransaction(2, 1, 2, bigDecimalValue10, categoryIds);

        List<CategoryAmountModel> expenseResults = subj.fetchExpenseByCategory(2, startDate, endDate);

        assertNotNull(expenseResults);
        assertFalse(expenseResults.isEmpty());
        assertEquals(1, expenseResults.size());
        assertEquals(bigDecimalValue10, expenseResults.get(0).getTotalAmount());
        assertNotNull(expenseResults);
        assertFalse(expenseResults.isEmpty());
        assertEquals(1, expenseResults.size());
        assertEquals("education", expenseResults.get(0).getCategoryName());

        categoryIds.add(3);
        subj.addTransaction(2, 1, 2, bigDecimalValue20, categoryIds);

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
                    subj.addTransaction(2, 1, 2, amount, categoryIds);
                });
    }
}