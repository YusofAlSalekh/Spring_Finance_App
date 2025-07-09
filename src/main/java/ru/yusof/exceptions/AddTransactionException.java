package ru.yusof.exceptions;

import java.sql.SQLException;

public class AddTransactionException extends RuntimeException {
    public AddTransactionException(String message, SQLException e) {
    }
}