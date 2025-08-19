package ru.yusof.exceptions;

public class GetExpenseByCategoryException extends RuntimeException{
    public GetExpenseByCategoryException(String message) {
        super(message);
    }
}