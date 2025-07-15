package ru.yusof.json.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yusof.service.TransactionCategoryDTO;

import java.util.List;

@AllArgsConstructor
@Data
public class TransactionCategoryViewingResponse {
    private List<TransactionCategoryDTO> transactionCategory;
}
