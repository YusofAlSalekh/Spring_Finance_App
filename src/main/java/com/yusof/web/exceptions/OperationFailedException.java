package com.yusof.web.exceptions;

public class OperationFailedException extends RuntimeException {
    public OperationFailedException(String message, Exception e) {
        super(message);
    }

    public OperationFailedException(String message) {
        super(message);
    }
}
