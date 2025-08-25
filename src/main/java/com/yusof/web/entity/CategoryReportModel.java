package com.yusof.web.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class CategoryReportModel {
    private String categoryName;
    private BigDecimal totalAmount;

    @Override
    public String toString() {
        return "CategoryAmount{" +
                "categoryName='" + categoryName + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryReportModel that = (CategoryReportModel) o;
        return Objects.equals(categoryName, that.categoryName) && Objects.equals(totalAmount, that.totalAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryName, totalAmount);
    }

    public CategoryReportModel(String categoryName, BigDecimal totalAmount) {
        this.categoryName = categoryName;
        this.totalAmount = totalAmount;
    }
}