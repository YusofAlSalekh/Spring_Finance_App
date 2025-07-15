package ru.yusof.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.request.TransactionCategoryCreationRequest;
import ru.yusof.json.response.TransactionCategoryCreationResponse;
import ru.yusof.service.TransactionCategoryDTO;
import ru.yusof.service.TransactionCategoryService;

import static ru.yusof.controller.ValidationUtils.validateNotBlankString;

@Service("/category/create")
@AllArgsConstructor
public class TransactionCategoryCreationController implements SecureController<TransactionCategoryCreationRequest, TransactionCategoryCreationResponse> {
    private final TransactionCategoryService transactionCategoryService;

    @Override
    public TransactionCategoryCreationResponse handle(TransactionCategoryCreationRequest request, Integer userId) {
        String categoryName = validateNotBlankString(request.getName());

        TransactionCategoryDTO transactionCategory = transactionCategoryService.createCategory(categoryName, userId);
        return new TransactionCategoryCreationResponse(transactionCategory);
    }

    @Override
    public Class<TransactionCategoryCreationRequest> getRequestClass() {
        return TransactionCategoryCreationRequest.class;
    }
}
