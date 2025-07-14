package ru.yusof.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.TransactionCategoryDeletionRequest;
import ru.yusof.json.TransactionCategoryDeletionResponse;
import ru.yusof.service.TransactionCategoryService;

import static ru.yusof.controller.ValidationUtils.extractPositiveInteger;

@AllArgsConstructor
@Service("/category/delete")
public class TransactionCategoryDeletionController implements SecureController<TransactionCategoryDeletionRequest, TransactionCategoryDeletionResponse> {
    private final TransactionCategoryService transactionCategoryService;

    @Override
    public TransactionCategoryDeletionResponse handle(TransactionCategoryDeletionRequest request, Integer userId) {
        Integer transactionCategoryId = extractPositiveInteger(request.getId());

        transactionCategoryService.deleteTransactionCategory(transactionCategoryId, userId);
        return new TransactionCategoryDeletionResponse("Transaction Category Deleted Successful");
    }

    @Override
    public Class<TransactionCategoryDeletionRequest> getRequestClass() {
        return TransactionCategoryDeletionRequest.class;
    }
}
