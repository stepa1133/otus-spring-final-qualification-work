package ru.otus.dutyschedule.exception;

import java.time.LocalDate;

/**
 * Недостаточно сотрудников для составления графика.
 * Например, в особом отделе всего 2 человека, оба на больничном.
 */
public class NotEnoughEmployeesException extends RuntimeException {

    public NotEnoughEmployeesException(String departmentName, LocalDate date) {
        super("Недостаточно сотрудников в отделе '" + departmentName +
                "' на дату " + date + ". Все отсутствуют или уволены.");
    }

    public NotEnoughEmployeesException(String message) {
        super(message);
    }
}