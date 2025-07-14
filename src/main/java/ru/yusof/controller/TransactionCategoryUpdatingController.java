package ru.yusof.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.TransactionCategoryUpdatingRequest;
import ru.yusof.json.TransactionCategoryUpdatingResponse;
import ru.yusof.service.TransactionCategoryService;

import static ru.yusof.controller.ValidationUtils.extractNotBlankString;
import static ru.yusof.controller.ValidationUtils.extractPositiveInteger;

@AllArgsConstructor
@Service("/category/update")
public class TransactionCategoryUpdatingController implements SecureController<TransactionCategoryUpdatingRequest, TransactionCategoryUpdatingResponse> {
    private final TransactionCategoryService transactionCategoryService;

    @Override
    public TransactionCategoryUpdatingResponse handle(TransactionCategoryUpdatingRequest request, Integer userId) {
        String newName = extractNotBlankString(request.getName());
        int transactionCategoryId = extractPositiveInteger(request.getId());

        transactionCategoryService.editTransactionCategory(newName, transactionCategoryId, userId);
        return new TransactionCategoryUpdatingResponse(transactionCategoryId, newName);
    }

    @Override
    public Class<TransactionCategoryUpdatingRequest> getRequestClass() {
        return TransactionCategoryUpdatingRequest.class;
    }
}
