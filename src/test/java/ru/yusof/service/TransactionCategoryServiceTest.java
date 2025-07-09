package ru.yusof.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yusof.converter.TransactionCategoryModelToTransactionCategoryDtoConverter;
import ru.yusof.dao.TransactionCategoryDao;
import ru.yusof.dao.TransactionCategoryModel;
import ru.yusof.exceptions.AddingTransactionCategoryException;
import ru.yusof.exceptions.CreatingTransactionCategoryException;
import ru.yusof.exceptions.DeletionTransactionCategoryException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionCategoryServiceTest {
    @InjectMocks
    TransactionCategoryService subj;

    @Mock
    TransactionCategoryDao transactionCategoryDao;

    @Mock
    TransactionCategoryModelToTransactionCategoryDtoConverter transactionCategoryModelToTransactionCategoryDtoConverter;

    @Test
    void categoryCreated() {
        TransactionCategoryModel transactionCategoryModel = new TransactionCategoryModel();
        transactionCategoryModel.setClientId(1);
        transactionCategoryModel.setName("name");
        transactionCategoryModel.setId(1);
        when(transactionCategoryDao.createTransactionCategory("name", 1)).thenReturn(transactionCategoryModel);

        TransactionCategoryDTO transactionCategoryDTO = new TransactionCategoryDTO();
        transactionCategoryDTO.setClientId(1);
        transactionCategoryDTO.setId(1);
        transactionCategoryDTO.setName("name");
        when(transactionCategoryModelToTransactionCategoryDtoConverter.convert(transactionCategoryModel)).thenReturn(transactionCategoryDTO);

        TransactionCategoryDTO transaction = subj.createCategory("name", 1);
        assertNotNull(transaction);
        assertEquals(transaction, transactionCategoryDTO);

        verify(transactionCategoryDao, times(1)).createTransactionCategory("name", 1);
        verify(transactionCategoryModelToTransactionCategoryDtoConverter, times(1)).convert(transactionCategoryModel);
    }

    @Test
    void categoryWasNotCreated() {
        when(transactionCategoryDao.createTransactionCategory("name", 1)).thenThrow(new CreatingTransactionCategoryException("Something went wrong during generating new category. Please, try again later!"));

        assertThrows(CreatingTransactionCategoryException.class, () -> subj.createCategory("name", 1), "Something went wrong during generating new category. Please, try again later!");

        verify(transactionCategoryDao, times(1)).createTransactionCategory("name", 1);
        verifyNoInteractions(transactionCategoryModelToTransactionCategoryDtoConverter);
    }

    @Test
    void transactionTypeWasDeleted() {
        when(transactionCategoryDao.deleteTransactionCategory(1, 1)).thenReturn(true);
        boolean a = subj.deleteTransactionCategory(1, 1);

        assertTrue(a);

        verify(transactionCategoryDao, times(1)).deleteTransactionCategory(1, 1);
    }

    @Test
    void transactionTypeWasNotDeleted() {
        when(transactionCategoryDao.deleteTransactionCategory(1, 1)).thenThrow(new DeletionTransactionCategoryException("Something went wrong. Please, try to delete again later!"));

        assertThrows(DeletionTransactionCategoryException.class, () -> subj.deleteTransactionCategory(1, 1), "Something went wrong. Please, try to delete again later!");

        verify(transactionCategoryDao, times(1)).deleteTransactionCategory(1, 1);
    }

    @Test
    void transactionTypeWasEdited() {
        when(transactionCategoryDao.editTransactionCategory("name", 1, 1)).thenReturn(true);

        assertTrue(subj.editTransactionCategory("name", 1, 1));

        verify(transactionCategoryDao, times(1)).editTransactionCategory("name", 1, 1);
    }

    @Test
    void transactionTypeWasNotEdited() {
        when(transactionCategoryDao.editTransactionCategory("name", 1, 1)).thenThrow(new AddingTransactionCategoryException("Something went wrong during editing. Please, try again later!"));

        assertThrows(AddingTransactionCategoryException.class, () -> subj.editTransactionCategory("name", 1, 1), "Something went wrong. Please, try to delete again later!");

        verify(transactionCategoryDao, times(1)).editTransactionCategory("name", 1, 1);
    }
}