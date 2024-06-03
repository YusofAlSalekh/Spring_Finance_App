package ru.yusof.exceptions;

import java.sql.SQLException;

public class CustomException extends RuntimeException {
    public CustomException(Throwable cause) {
        super(cause);
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, SQLException e) {
    }
}