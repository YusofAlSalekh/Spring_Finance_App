package ru.yusof.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yusof.converter.TransactionTypeModelToTransactionDtoConverter;
import ru.yusof.dao.CategoryAmountModel;
import ru.yusof.dao.TransactionDao;
import ru.yusof.dao.TransactionTypeModel;
import ru.yusof.exceptions.*;

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
    @Mock
    TransactionTypeModelToTransactionDtoConverter transactionTypeModelToTransactionDtoConverter;

    @Test
    void categoryCreated() {
        TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
        transactionTypeModel.setClientId(1);
        transactionTypeModel.setName("name");
        transactionTypeModel.setId(1);
        when(transactionDao.createTransactionType("name", 1)).thenReturn(transactionTypeModel);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setClient_id(1);
        transactionDTO.setId(1);
        transactionDTO.setName("name");
        when(transactionTypeModelToTransactionDtoConverter.convert(transactionTypeModel)).thenReturn(transactionDTO);

        TransactionDTO transaction = subj.createCategory("name", 1);
        assertNotNull(transaction);
        assertEquals(transaction, transactionDTO);

        verify(transactionDao, times(1)).createTransactionType("name", 1);
        verify(transactionTypeModelToTransactionDtoConverter, times(1)).convert(transactionTypeModel);
    }

    @Test
    void categoryWasNotCreated() {
        when(transactionDao.createTransactionType("name", 1)).thenThrow(new CreatingTransactionTypeException("Something went wrong during generating new category. Please, try again later!"));

        assertThrows(CreatingTransactionTypeException.class, () -> subj.createCategory("name", 1), "Something went wrong during generating new category. Please, try again later!");

        verify(transactionDao, times(1)).createTransactionType("name", 1);
        verifyNoInteractions(transactionTypeModelToTransactionDtoConverter);
    }

    @Test
    void transactionTypeWasDeleted() {
        when(transactionDao.deleteTransactionType(1, 1)).thenReturn(true);
        boolean a = subj.deleteTransactionType(1, 1);

        assertTrue(a);

        verify(transactionDao, times(1)).deleteTransactionType(1, 1);
    }

    @Test
    void transactionTypeWasNotDeleted() {
        when(transactionDao.deleteTransactionType(1, 1)).thenThrow(new DeletionTransactionTypeException("Something went wrong. Please, try to delete again later!"));

        assertThrows(DeletionTransactionTypeException.class, () -> subj.deleteTransactionType(1, 1), "Something went wrong. Please, try to delete again later!");

        verify(transactionDao, times(1)).deleteTransactionType(1, 1);
    }

    @Test
    void transactionTypeWasEdited() {
        when(transactionDao.editTransactionType("name", 1, 1)).thenReturn(true);

        assertTrue(subj.editTransactionType("name", 1, 1));

        verify(transactionDao, times(1)).editTransactionType("name", 1, 1);
    }

    @Test
    void transactionTypeWasNotEdited() {
        when(transactionDao.editTransactionType("name", 1, 1)).thenThrow(new AddingTransactionTypeException("Something went wrong during editing. Please, try again later!"));

        assertThrows(AddingTransactionTypeException.class, () -> subj.editTransactionType("name", 1, 1), "Something went wrong. Please, try to delete again later!");

        verify(transactionDao, times(1)).editTransactionType("name", 1, 1);
    }

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