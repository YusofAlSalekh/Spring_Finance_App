package ru.yusof.exceptions;

public class AddingTransactionTypeException extends RuntimeException {
    public AddingTransactionTypeException(String message) {
        super(message);
    }
}