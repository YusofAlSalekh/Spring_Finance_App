package ru.yusof.dao;

import java.math.BigDecimal;
import java.util.Objects;

public class CategoryAmountModel {
    private String categoryName;
    private BigDecimal totalAmount;

    public String getCategoryName() {
        return categoryName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

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
        CategoryAmountModel that = (CategoryAmountModel) o;
        return Objects.equals(categoryName, that.categoryName) && Objects.equals(totalAmount, that.totalAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryName, totalAmount);
    }
}