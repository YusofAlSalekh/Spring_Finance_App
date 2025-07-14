package ru.yusof.controller;

import java.math.BigDecimal;
import java.util.List;

public class ValidationUtils {
    protected static String extractNotBlankString(String paramName) {
        if (paramName == null || paramName.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        return paramName;
    }

    protected static BigDecimal extractPositiveBigDecimal(BigDecimal value) {
        if (value == null || value.signum() < 0) {
            throw new IllegalArgumentException("A positive number must be provided");
        }
        return value;
    }

    protected static Integer extractPositiveInteger(Integer value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("A positive number must be provided");
        }
        return value;
    }

    protected static List<Integer> extractIntegerList(List<Integer> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException(" Values must be provided");
        }

        for (Integer value : values) {
            extractPositiveInteger(value);
        }
        return values;
    }
}
