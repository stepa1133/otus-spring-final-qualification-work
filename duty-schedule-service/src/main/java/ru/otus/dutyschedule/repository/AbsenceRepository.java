package ru.otus.dutyschedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.dutyschedule.model.Absence;
import ru.otus.dutyschedule.model.Employee;

import java.time.LocalDate;
import java.util.List;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {

    /** Все отсутствия сотрудника */
    List<Absence> findAllByEmployee(Employee employee);

    /** Отсутствия сотрудника за период */
    List<Absence> findAllByEmployeeAndStartDateBetween(
            Employee employee, LocalDate from, LocalDate to);

    /** Проверить, отсутствует ли сотрудник в конкретную дату */
    @Query("SELECT COUNT(a) > 0 FROM Absence a " +
            "WHERE a.employee = :employee " +
            "AND :date BETWEEN a.startDate AND a.endDate")
    boolean isAbsentOnDate(@Param("employee") Employee employee,
                           @Param("date") LocalDate date);

    /** Найти всех сотрудников, которые отсутствуют в указанную дату */
    @Query("SELECT a.employee FROM Absence a " +
            "WHERE :date BETWEEN a.startDate AND a.endDate")
    List<Employee> findAbsentEmployeesOnDate(@Param("date") LocalDate date);
}