package com.yusof.web.api.json.response;

import com.yusof.web.entity.CategoryReportModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ReportResponse {
    private String categoryName;
    private BigDecimal totalAmount;

    public static ReportResponse fromModel(CategoryReportModel model) {
        return new ReportResponse(model.getCategoryName(), model.getTotalAmount());
    }
}
