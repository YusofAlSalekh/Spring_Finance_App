package ru.yusof.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.request.TransactionCategoryUpdatingRequest;
import ru.yusof.json.response.TransactionCategoryUpdatingResponse;
import ru.yusof.service.TransactionCategoryService;

import static ru.yusof.controller.ValidationUtils.validateNotBlankString;
import static ru.yusof.controller.ValidationUtils.validatePositiveInteger;

@AllArgsConstructor
@Service("/category/update")
public class TransactionCategoryUpdatingController implements SecureController<TransactionCategoryUpdatingRequest, TransactionCategoryUpdatingResponse> {
    private final TransactionCategoryService transactionCategoryService;

    @Override
    public TransactionCategoryUpdatingResponse handle(TransactionCategoryUpdatingRequest request, Integer userId) {
        String newName = validateNotBlankString(request.getName());
        int transactionCategoryId = validatePositiveInteger(request.getId());

        transactionCategoryService.editTransactionCategory(newName, transactionCategoryId, userId);
        return new TransactionCategoryUpdatingResponse(transactionCategoryId, newName);
    }

    @Override
    public Class<TransactionCategoryUpdatingRequest> getRequestClass() {
        return TransactionCategoryUpdatingRequest.class;
    }
}
