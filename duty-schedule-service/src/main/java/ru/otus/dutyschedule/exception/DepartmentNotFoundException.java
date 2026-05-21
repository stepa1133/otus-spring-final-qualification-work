package ru.otus.dutyschedule.exception;

/**
 * Отдел не найден.
 */
public class DepartmentNotFoundException extends RuntimeException {

    public DepartmentNotFoundException(Long id) {
        super("Отдел с id=" + id + " не найден");
    }

    public DepartmentNotFoundException(String name) {
        super("Отдел с названием '" + name + "' не найден");
    }
}