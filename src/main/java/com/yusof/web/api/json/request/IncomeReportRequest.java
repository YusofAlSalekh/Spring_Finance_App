package com.yusof.web.api.json.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IncomeReportRequest {
    @NotNull
    @PastOrPresent
    private LocalDate startDate;

    @NotNull
    @PastOrPresent
    private LocalDate endDate;
}
