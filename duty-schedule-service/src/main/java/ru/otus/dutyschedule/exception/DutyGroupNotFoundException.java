package ru.otus.dutyschedule.exception;

/**
 * Группа дежурств не найдена.
 */
public class DutyGroupNotFoundException extends RuntimeException {

    public DutyGroupNotFoundException(Long id) {
        super("График дежурств с id=" + id + " не найден");
    }
}