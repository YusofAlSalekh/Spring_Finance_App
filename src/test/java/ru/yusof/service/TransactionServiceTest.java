package ru.yusof.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yusof.dao.CategoryAmountModel;
import ru.yusof.dao.TransactionDao;
import ru.yusof.exceptions.GetExpenseByCategoryException;
import ru.yusof.exceptions.GetIncomeByCategoryException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @InjectMocks
    TransactionService subj;
    @Mock
    TransactionDao transactionDao;

    @Test
    void ReportOverIncomeWorks() {
        CategoryAmountModel categoryAmountModel = new CategoryAmountModel();
        categoryAmountModel.setCategoryName("name");
        categoryAmountModel.setTotalAmount(BigDecimal.valueOf(100));
        List<CategoryAmountModel> categoryAmountModels = Arrays.asList(categoryAmountModel);
        when(transactionDao.fetchIncomeByCategory(1, LocalDate.parse("2024-05-25"), LocalDate.parse("2024-05-30"))).thenReturn(categoryAmountModels);

        List<CategoryAmountModel> list = subj.getIncomeReportByCategory(1, LocalDate.parse("2024-05-25"), LocalDate.parse("2024-05-30"));

        assertNotNull(list);
        assertEquals(list, categoryAmountModels);
    }

    @Test
    void ReportOverExpensesWorks() {
        CategoryAmountModel categoryAmountModel = new CategoryAmountModel();
        categoryAmountModel.setCategoryName("name");
        categoryAmountModel.setTotalAmount(BigDecimal.valueOf(100));
        List<CategoryAmountModel> categoryAmountModels = Arrays.asList(categoryAmountModel);
        when(transactionDao.fetchExpenseByCategory(1, LocalDate.parse("2024-05-25"), LocalDate.parse("2024-05-30"))).thenReturn(categoryAmountModels);

        List<CategoryAmountModel> list = subj.getExpenseReportByCategory(1, LocalDate.parse("2024-05-25"), LocalDate.parse("2024-05-30"));

        assertNotNull(list);
        assertEquals(list, categoryAmountModels);
    }

    @Test
    void ReportOverExpensesDoesntWork() {
        when(transactionDao.fetchExpenseByCategory(1, LocalDate.parse("2024-05-25"), LocalDate.parse("2024-05-30"))).thenThrow(new GetExpenseByCategoryException("The expense report failed to be displayed"));

        assertThrows(GetExpenseByCategoryException.class, () -> subj.getExpenseReportByCategory(1, LocalDate.parse("2024-05-25"), LocalDate.parse("2024-05-30")), "The expense report failed to be displayed");

        verify(transactionDao, times(1)).fetchExpenseByCategory(1, LocalDate.parse("2024-05-25"), LocalDate.parse("2024-05-30"));
    }

    @Test
    void ReportOverIncomeDoesntWork() {
        when(transactionDao.fetchIncomeByCategory(1, LocalDate.parse("2024-05-25"), LocalDate.parse("2024-05-30"))).thenThrow(new GetIncomeByCategoryException("The income report failed to be displayed"));

        assertThrows(GetIncomeByCategoryException.class, () -> subj.getIncomeReportByCategory(1, LocalDate.parse("2024-05-25"), LocalDate.parse("2024-05-30")), "The income report failed to be displayed");

        verify(transactionDao, times(1)).fetchIncomeByCategory(1, LocalDate.parse("2024-05-25"), LocalDate.parse("2024-05-30"));
    }
}