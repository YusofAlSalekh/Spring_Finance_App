package com.yusof.web.web.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseReportForm {
    @NotNull
    @PastOrPresent
    private LocalDate startDate;

    @NotNull
    @PastOrPresent
    private LocalDate endDate;
}
