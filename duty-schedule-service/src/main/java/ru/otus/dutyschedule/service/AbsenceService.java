package ru.otus.dutyschedule.service;

import ru.otus.dutyschedule.dto.request.AbsenceRequest;
import ru.otus.dutyschedule.model.Absence;
import ru.otus.dutyschedule.model.Employee;

import java.time.LocalDate;
import java.util.List;

public interface AbsenceService {

    /** Зарегистрировать отсутствие */
    Absence create(AbsenceRequest request);

    /** Отсутствия сотрудника */
    List<Absence> getByEmployee(Long employeeId);

    /** Проверить, отсутствует ли сотрудник в дату */
    boolean isAbsent(Employee employee, LocalDate date);

    /** Список сотрудников, отсутствующих в дату */
    List<Employee> getAbsentEmployees(LocalDate date);
}