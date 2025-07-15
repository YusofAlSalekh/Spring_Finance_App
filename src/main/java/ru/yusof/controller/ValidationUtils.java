package ru.yusof.controller;

import java.math.BigDecimal;
import java.util.List;

public class ValidationUtils {
    protected static String validateNotBlankString(String paramName) {
        if (paramName == null || paramName.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        return paramName;
    }

    protected static BigDecimal validatePositiveBigDecimal(BigDecimal value) {
        if (value == null || value.signum() < 0) {
            throw new IllegalArgumentException("A positive number must be provided");
        }
        return value;
    }

    protected static Integer validatePositiveInteger(Integer value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("A positive number must be provided");
        }
        return value;
    }

    protected static List<Integer> validateIntegerList(List<Integer> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException(" Values must be provided");
        }

        for (Integer value : values) {
            validatePositiveInteger(value);
        }
        return values;
    }
}
