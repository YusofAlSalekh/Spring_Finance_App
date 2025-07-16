package ru.yusof.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.request.TransactionCategoryViewingRequest;
import ru.yusof.json.response.TransactionCategoryViewingResponse;
import ru.yusof.service.TransactionCategoryDTO;
import ru.yusof.service.TransactionCategoryService;

import java.util.List;

@AllArgsConstructor
@Service("/category/show")
public class TransactionCategoryViewingController implements SecureController<TransactionCategoryViewingRequest, TransactionCategoryViewingResponse> {
    private final TransactionCategoryService transactionCategoryService;

    @Override
    public TransactionCategoryViewingResponse handle(TransactionCategoryViewingRequest request, Integer userId) {
        List<TransactionCategoryDTO> transactionCategory = transactionCategoryService.viewTransactionCategory(userId);
        return new TransactionCategoryViewingResponse(transactionCategory);
    }

    @Override
    public Class<TransactionCategoryViewingRequest> getRequestClass() {
        return TransactionCategoryViewingRequest.class;
    }
}
