package ru.otus.dutyschedule.exception;

/**
 * Сотрудник не найден.
 */
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Long id) {
        super("Сотрудник с id=" + id + " не найден");
    }

    public EmployeeNotFoundException(String email) {
        super("Сотрудник с email '" + email + "' не найден");
    }
}