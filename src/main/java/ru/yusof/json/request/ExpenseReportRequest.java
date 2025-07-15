package ru.yusof.json.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseReportRequest {
   private LocalDate startDate;
   private LocalDate endDate;
}
