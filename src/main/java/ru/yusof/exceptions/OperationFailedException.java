package ru.yusof.exceptions;

import java.sql.SQLException;

public class OperationFailedException extends RuntimeException {
    public OperationFailedException(String message, SQLException e) {
        super(message);
    }

    public OperationFailedException(String message) {
        super(message);
    }
}
