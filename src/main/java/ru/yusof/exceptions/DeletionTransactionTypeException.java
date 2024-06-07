package ru.yusof.exceptions;

public class DeletionTransactionTypeException extends RuntimeException{
    public DeletionTransactionTypeException(String message) {
        super(message);
    }
}