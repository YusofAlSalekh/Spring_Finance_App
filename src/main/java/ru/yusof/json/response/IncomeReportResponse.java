package ru.yusof.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yusof.dao.CategoryAmountModel;

import java.util.List;

@Data
@AllArgsConstructor
public class IncomeReportResponse {
    private List<CategoryAmountModel> transactions;
}
