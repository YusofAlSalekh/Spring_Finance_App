package ru.yusof.exceptions;

public class AddingTransactionCategoryException extends RuntimeException {
    public AddingTransactionCategoryException(String message) {
        super(message);
    }
}