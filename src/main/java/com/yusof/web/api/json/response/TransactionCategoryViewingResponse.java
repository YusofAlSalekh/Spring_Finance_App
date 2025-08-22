package com.yusof.web.api.json.response;

import com.yusof.web.service.TransactionCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class TransactionCategoryViewingResponse {
    private List<TransactionCategoryDTO> transactionCategory;
}
