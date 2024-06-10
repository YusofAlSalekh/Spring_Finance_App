package ru.yusof.exceptions;

public class GetIncomeByCategoryException extends RuntimeException{
    public GetIncomeByCategoryException(String message) {
        super(message);
    }
}