package com.yusof.web.api.json.response;

import com.yusof.web.service.TransactionCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionCategoryCreationResponse {
    private TransactionCategoryDTO transactionCategory;
}
