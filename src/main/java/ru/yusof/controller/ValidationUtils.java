package ru.yusof.controller;

import java.math.BigDecimal;
import java.util.List;

public class ValidationUtils {
    protected static void validateNotBlankString(String paramName) {
        if (paramName == null || paramName.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
    }

    protected static void validatePositiveBigDecimal(BigDecimal value) {
        if (value == null || value.signum() < 0) {
            throw new IllegalArgumentException("A positive number must be provided");
        }
    }

    protected static void validatePositiveInteger(Integer value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("A positive number must be provided");
        }
    }

    protected static void validateIntegerList(List<Integer> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException(" Values must be provided");
        }

        for (Integer value : values) {
            validatePositiveInteger(value);
        }
    }
}
