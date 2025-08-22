package com.yusof.web.api.json.response;

import com.yusof.web.entity.CategoryAmountModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IncomeReportResponse {
    private List<CategoryAmountModel> transactions;
}
