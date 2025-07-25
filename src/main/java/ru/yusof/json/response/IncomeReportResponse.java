package ru.yusof.json.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yusof.entity.CategoryAmountModel;

import java.util.List;

@Data
@AllArgsConstructor
public class IncomeReportResponse {
    private List<CategoryAmountModel> transactions;
}
