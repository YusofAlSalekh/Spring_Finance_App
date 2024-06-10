package ru.yusof.exceptions;

public class RegistrationOfANewUserException extends RuntimeException{
    public RegistrationOfANewUserException(String message) {
        super(message);
    }
}