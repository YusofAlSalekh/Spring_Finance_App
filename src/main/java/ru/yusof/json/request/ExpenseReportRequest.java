package ru.yusof.json;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseReportRequest {
    LocalDate startDate;
    LocalDate endDate;
}
