package ru.otus.dutyschedule.exception;

/**
 * Email уже занят другим сотрудником.
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Сотрудник с email '" + email + "' уже существует");
    }
}