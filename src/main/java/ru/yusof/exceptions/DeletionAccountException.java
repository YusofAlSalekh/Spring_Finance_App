package ru.yusof.exceptions;

public class DeletionAccountException extends RuntimeException{
    public DeletionAccountException(String message) {
        super(message);
    }
}