package ru.yusof.json;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IncomeReportRequest {
private LocalDate startDate;
private LocalDate endDate;
}
