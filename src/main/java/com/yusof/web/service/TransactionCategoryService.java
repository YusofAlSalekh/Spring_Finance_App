package com.yusof.web.service;

import com.yusof.web.entity.TransactionCategoryModel;
import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.repository.TransactionCategoryRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Validated
public class TransactionCategoryService {
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final Converter<TransactionCategoryModel, TransactionCategoryDTO> transactionCategoryDTOConverter;

    @Transactional
    public TransactionCategoryDTO createCategory(@NotBlank String categoryName, int clientId) {
        ensureNameUniqueForCreation(categoryName, clientId);
        TransactionCategoryModel transactionCategoryModel = getTransactionCategoryModel(categoryName, clientId);
        transactionCategoryRepository.save(transactionCategoryModel);

        return transactionCategoryDTOConverter.convert(transactionCategoryModel);
    }

    @Transactional
    public void deleteTransactionCategory(@NotNull @Positive int transactionCategoryId, int clientId) {
        assertTransactionCategoryExists(transactionCategoryId, clientId);
        transactionCategoryRepository.deleteByIdAndClientId(transactionCategoryId, clientId);
    }

    @Transactional
    public TransactionCategoryDTO updateTransactionCategory(@NotBlank String newName, @NotNull @Positive int transactionCategoryId, int clientId) {
        ensureNameUniqueForUpdating(newName, transactionCategoryId, clientId);
        TransactionCategoryModel transactionCategoryModel = assertTransactionCategoryExists(transactionCategoryId, clientId);
        transactionCategoryModel.setName(newName);

        return transactionCategoryDTOConverter.convert(transactionCategoryModel);
    }

    public List<TransactionCategoryDTO> viewTransactionCategory(int clientId) {
        List<TransactionCategoryModel> transactionCategoryModels = transactionCategoryRepository.findByClientId(clientId);

        return transactionCategoryModels.stream()
                .map(transactionCategoryDTOConverter::convert)
                .collect(Collectors.toList());
    }

    private TransactionCategoryModel assertTransactionCategoryExists(int transactionCategoryId, int clientId) {
        return transactionCategoryRepository.findByIdAndClientId(transactionCategoryId, clientId)
                .orElseThrow(() -> new NotFoundException("No transaction category found with Id: " + transactionCategoryId));
    }

    private static TransactionCategoryModel getTransactionCategoryModel(String categoryName, int clientId) {
        TransactionCategoryModel transactionCategoryModel = new TransactionCategoryModel();
        transactionCategoryModel.setName(categoryName);
        transactionCategoryModel.setClientId(clientId);
        return transactionCategoryModel;
    }

    private void ensureNameUniqueForCreation(String categoryName, int clientId) {
        if (transactionCategoryRepository.existsByClientIdAndNameIgnoreCase(clientId, categoryName)) {
            throw new AlreadyExistsException("Category \"" + categoryName + "\" already exists");
        }
    }

    private void ensureNameUniqueForUpdating(String newName, int transactionCategoryId, int clientId) {
        int count = transactionCategoryRepository.countByNameAndClientIdAndIdNot(newName, clientId, transactionCategoryId);

        if (count > 0) {
            throw new AlreadyExistsException("Transaction category with this name already exists");
        }
    }
}
