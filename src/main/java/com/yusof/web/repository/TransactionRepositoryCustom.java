package com.yusof.web.repository;

import com.yusof.web.entity.CategoryReportModel;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepositoryCustom {
    List<CategoryReportModel> fetchIncomeByCategory(int clientId,
                                                    LocalDateTime start,
                                                    LocalDateTime end);

    List<CategoryReportModel> fetchExpenseByCategory(int clientId,
                                                     LocalDateTime start,
                                                     LocalDateTime end);
}

