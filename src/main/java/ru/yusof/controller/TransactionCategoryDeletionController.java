package ru.yusof.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.request.TransactionCategoryDeletionRequest;
import ru.yusof.json.response.TransactionCategoryDeletionResponse;
import ru.yusof.service.TransactionCategoryService;

import static ru.yusof.controller.ValidationUtils.validatePositiveInteger;

@AllArgsConstructor
@Service("/category/delete")
public class TransactionCategoryDeletionController implements SecureController<TransactionCategoryDeletionRequest, TransactionCategoryDeletionResponse> {
    private final TransactionCategoryService transactionCategoryService;

    @Override
    public TransactionCategoryDeletionResponse handle(TransactionCategoryDeletionRequest request, Integer userId) {
        validatePositiveInteger(request.getId());
        Integer transactionCategoryId = request.getId();

        transactionCategoryService.deleteTransactionCategory(transactionCategoryId, userId);
        return new TransactionCategoryDeletionResponse("Transaction Category Deleted Successful");
    }

    @Override
    public Class<TransactionCategoryDeletionRequest> getRequestClass() {
        return TransactionCategoryDeletionRequest.class;
    }
}
