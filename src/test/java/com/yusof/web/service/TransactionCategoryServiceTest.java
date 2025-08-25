package com.yusof.web.service;

import com.yusof.web.entity.TransactionCategoryModel;
import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.repository.TransactionCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionCategoryServiceTest {
    @InjectMocks
    TransactionCategoryService subject;

    @Mock
    TransactionCategoryRepository transactionCategoryRepository;

    @Mock
    Converter<TransactionCategoryModel, TransactionCategoryDTO> transactionCategoryDTOConverter;

    @Test
    void createCategory_success() {
        int clientId = 1;
        String name = "Food";
        TransactionCategoryDTO dto = new TransactionCategoryDTO();

        when(transactionCategoryRepository.existsByClientIdAndNameIgnoreCase(clientId, name)).thenReturn(false);
        when(transactionCategoryDTOConverter.convert(any(TransactionCategoryModel.class))).thenReturn(dto);

        TransactionCategoryDTO result = subject.createCategory(name, clientId);

        assertEquals(dto, result);
        verify(transactionCategoryRepository).save(any(TransactionCategoryModel.class));
        verify(transactionCategoryDTOConverter).convert(any(TransactionCategoryModel.class));
    }

    @Test
    void createCategory_throwsAlreadyExistException() {
        int clientId = 1;
        String name = "Food";

        when(transactionCategoryRepository.existsByClientIdAndNameIgnoreCase(clientId, name)).thenReturn(true);

        assertThrowsExactly(AlreadyExistsException.class,
                () -> subject.createCategory(name, clientId));

        verify(transactionCategoryRepository, never()).save(any());
        verify(transactionCategoryDTOConverter, never()).convert(any(TransactionCategoryModel.class));
    }

    @Test
    void deleteTransactionCategory_success() {
        int id = 1;
        int clientId = 1;
        TransactionCategoryModel model = new TransactionCategoryModel();
        model.setId(id);
        model.setClientId(clientId);

        when(transactionCategoryRepository.findByIdAndClientId(id, clientId)).thenReturn(Optional.of(model));

        assertDoesNotThrow(() -> subject.deleteTransactionCategory(id, clientId));

        verify(transactionCategoryRepository).deleteByIdAndClientId(id, clientId);
    }

    @Test
    void deleteTransactionCategory_categoryNotFound_throwsException() {
        int id = 1;
        int clientId = 1;
        when(transactionCategoryRepository.findByIdAndClientId(id, clientId)).thenReturn(Optional.empty());

        assertThrowsExactly(NotFoundException.class,
                () -> subject.deleteTransactionCategory(id, clientId));

        verify(transactionCategoryRepository, never()).deleteByIdAndClientId(id, clientId);
    }

    @Test
    void updateTransactionCategory_success() {
        int id = 11;
        int clientId = 1;
        String newName = "NewName";

        TransactionCategoryModel model = new TransactionCategoryModel();
        model.setId(id);
        model.setClientId(clientId);

        TransactionCategoryDTO dto = new TransactionCategoryDTO();

        when(transactionCategoryRepository.countByNameAndClientIdAndIdNot(newName, clientId, id)).thenReturn(0);
        when(transactionCategoryRepository.findByIdAndClientId(id, clientId)).thenReturn(Optional.of(model));
        when(transactionCategoryDTOConverter.convert(model)).thenReturn(dto);

        TransactionCategoryDTO result = subject.updateTransactionCategory(newName, id, clientId);

        assertEquals(dto, result);
        assertEquals(newName, model.getName());

        verify(transactionCategoryRepository).countByNameAndClientIdAndIdNot(newName, clientId, id);
        verify(transactionCategoryDTOConverter).convert(model);
        verifyNoMoreInteractions(transactionCategoryRepository, transactionCategoryDTOConverter);
    }

    @Test
    void updateTransactionCategory_throwsAlreadyExistsException() {
        int id = 1;
        int clientId = 1;
        String newName = "NewName";

        when(transactionCategoryRepository.countByNameAndClientIdAndIdNot(newName, clientId, id)).thenReturn(1);

        assertThrowsExactly(AlreadyExistsException.class,
                () -> subject.updateTransactionCategory(newName, id, clientId));

        verify(transactionCategoryDTOConverter, never()).convert(any(TransactionCategoryModel.class));
    }

    @Test
    void viewTransactionCategory_returnsConvertedList() {
        int clientId = 1;
        TransactionCategoryModel model = new TransactionCategoryModel();
        model.setId(1);
        model.setName("Food");
        model.setClientId(clientId);

        TransactionCategoryDTO dto = new TransactionCategoryDTO();

        when(transactionCategoryRepository.findByClientId(clientId))
                .thenReturn(List.of(model));
        when(transactionCategoryDTOConverter.convert(model)).thenReturn(dto);

        List<TransactionCategoryDTO> result = subject.viewTransactionCategory(clientId);

        assertEquals(List.of(dto), result);
        verify(transactionCategoryRepository).findByClientId(clientId);
        verify(transactionCategoryDTOConverter).convert(model);
    }

    @Test
    void viewTransactionCategory_empty_returnsEmptyList() {
        int clientId = 1;

        when(transactionCategoryRepository.findByClientId(clientId))
                .thenReturn(List.of());

        List<TransactionCategoryDTO> result = subject.viewTransactionCategory(clientId);

        assertTrue(result.isEmpty());
        verify(transactionCategoryRepository).findByClientId(clientId);
        verifyNoInteractions(transactionCategoryDTOConverter);
    }
}
