package ru.otus.dutyschedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.dutyschedule.model.Department;
import ru.otus.dutyschedule.model.Duty;
import ru.otus.dutyschedule.model.DutyGroup;
import ru.otus.dutyschedule.model.Employee;

import java.time.LocalDate;
import java.util.List;

public interface DutyRepository extends JpaRepository<Duty, Long> {

    /** Все дежурства в рамках одной группы */
    List<Duty> findAllByDutyGroup(DutyGroup dutyGroup);

    /** Дежурства конкретного сотрудника */
    List<Duty> findAllByEmployee(Employee employee);

    /** Дежурства сотрудника за период */
    List<Duty> findAllByEmployeeAndDateBetween(Employee employee, LocalDate from, LocalDate to);

    /** Дежурства на конкретную дату */
    List<Duty> findAllByDate(LocalDate date);

    /** Дежурства на дату в рамках группы (для перегенерации) */
    List<Duty> findAllByDutyGroupAndDate(DutyGroup dutyGroup, LocalDate date);

    /** Кто дежурит в определённый день из конкретного отдела */
    List<Duty> findAllByDateAndDepartment(LocalDate date, Department department);
}