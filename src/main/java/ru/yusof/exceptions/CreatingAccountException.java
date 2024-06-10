package ru.yusof.exceptions;

public class CreatingAccountException extends RuntimeException{
    public CreatingAccountException(String message) {
        super(message);
    }
}