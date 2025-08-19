package com.yusof.web.service;

import com.yusof.web.entity.TransactionCategoryModel;
import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.IllegalOwnerException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.exceptions.OperationFailedException;
import com.yusof.web.repository.TransactionCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionCategoryService {
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final Converter<TransactionCategoryModel, TransactionCategoryDTO> transactionCategoryDTOConverter;

    @Transactional
    public TransactionCategoryDTO createCategory(String categoryName, int clientId) {
        boolean exists = transactionCategoryRepository.existsByClientIdAndNameIgnoreCase(clientId, categoryName);

        if (!exists) {
            TransactionCategoryModel transactionCategoryModel = new TransactionCategoryModel();
            transactionCategoryModel.setName(categoryName);
            transactionCategoryModel.setClientId(clientId);
            transactionCategoryRepository.save(transactionCategoryModel);

            return transactionCategoryDTOConverter.convert(transactionCategoryModel);
        } else throw new AlreadyExistsException("Category \"" + categoryName + "\" already exists");
    }

    @Transactional
    public void deleteTransactionCategory(int transactionCategoryId, int clientId) {
        transactionCategoryExistenceValidation(transactionCategoryId, clientId);

        int modifiedCount = transactionCategoryRepository.deleteByIdAndClientId(transactionCategoryId, clientId);
        if (modifiedCount == 0) {
            throw new OperationFailedException("Error has occurred while deleting transaction category");
        }
    }

    @Transactional
    public TransactionCategoryDTO updateTransactionCategory(String newName, int transactionCategoryId, int clientId) {
        int count = transactionCategoryRepository.countByNameAndClientIdAndIdNot(newName, clientId, transactionCategoryId);

        if (count > 0) {
            throw new AlreadyExistsException("Transaction category with this name already exists");
        }

        TransactionCategoryModel transactionCategoryModel = transactionCategoryExistenceValidation(transactionCategoryId, clientId);

        transactionCategoryModel.setName(newName);
        return transactionCategoryDTOConverter.convert(transactionCategoryModel);
    }

    public List<TransactionCategoryDTO> viewTransactionCategory(int clientId) {
        List<TransactionCategoryModel> transactionCategoryModels = transactionCategoryRepository.findByClientId(clientId);
        List<TransactionCategoryDTO> transactionCategoryDTOs = new ArrayList<>();

        for (TransactionCategoryModel transactionCategoryModel : transactionCategoryModels) {
            transactionCategoryDTOs.add(transactionCategoryDTOConverter.convert(transactionCategoryModel));
        }
        return transactionCategoryDTOs;
    }

    private TransactionCategoryModel transactionCategoryExistenceValidation(int transactionCategoryId, int clientId) {
        TransactionCategoryModel transactionCategoryModel = transactionCategoryRepository.findById(transactionCategoryId)
                .orElseThrow(() -> new NotFoundException("No transaction category found with Id: " + transactionCategoryId));

        if (transactionCategoryModel.getClientId() != clientId) {
            throw new IllegalOwnerException("Transaction category with Id: " + transactionCategoryId + " belongs to another client");
        }

        return transactionCategoryModel;
    }
}
