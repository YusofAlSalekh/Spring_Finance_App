package com.yusof.web.repository;

import com.yusof.web.entity.CategoryReportModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.liquibase.contexts=test"
})
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void fetchIncomeByCategory() {
        LocalDateTime start = LocalDateTime.parse("2025-09-04T10:00:00");
        LocalDateTime end = LocalDateTime.parse("2025-09-08T10:00:00");

        List<CategoryReportModel> result =
                transactionRepository.fetchIncomeByCategory(1, start, end);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCategoryName()).isEqualTo("Cleaning");
        assertThat(result.get(0).getTotalAmount()).isEqualByComparingTo("10.00");
        assertThat(result.get(1).getCategoryName()).isEqualTo("Food");
        assertThat(result.get(1).getTotalAmount()).isEqualByComparingTo("20.00");
    }

    @Test
    void fetchExpenseByCategory() {
        LocalDateTime start = LocalDateTime.parse("2025-09-06T10:00:00");
        LocalDateTime end = LocalDateTime.parse("2025-09-08T10:00:00");

        List<CategoryReportModel> result =
                transactionRepository.fetchIncomeByCategory(1, start, end);

        assertThat(result).hasSize(1);
        CategoryReportModel report = result.get(0);
        assertThat(report.getCategoryName()).isEqualTo("Food");
        assertThat(report.getTotalAmount()).isEqualByComparingTo("20.00");
    }
}