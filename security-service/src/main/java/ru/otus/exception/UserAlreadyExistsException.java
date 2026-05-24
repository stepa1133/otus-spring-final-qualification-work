package ru.otus.exception;

public class UserAlreadyExistsException   extends RuntimeException {
    public UserAlreadyExistsException() {
        super("Username already exists");
    }
}
