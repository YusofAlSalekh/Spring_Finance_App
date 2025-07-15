package ru.yusof.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yusof.service.TransactionCategoryDTO;

@Data
@AllArgsConstructor
public class TransactionCategoryCreationResponse {
    TransactionCategoryDTO transactionCategory;
}
