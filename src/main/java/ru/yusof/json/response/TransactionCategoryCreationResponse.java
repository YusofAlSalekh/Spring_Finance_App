package ru.yusof.json.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yusof.service.TransactionCategoryDTO;

@Data
@AllArgsConstructor
public class TransactionCategoryCreationResponse {
    private TransactionCategoryDTO transactionCategory;
}
