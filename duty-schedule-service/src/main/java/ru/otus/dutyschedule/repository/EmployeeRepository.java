package ru.otus.dutyschedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.dutyschedule.model.Department;
import ru.otus.dutyschedule.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /** Найти по email (логину) */
    Optional<Employee> findByEmail(String email);

    /** Все активные сотрудники отдела */
    List<Employee> findAllByDepartmentAndActiveTrue(Department department);

    /** Все активные сотрудники (не уволенные) */
    List<Employee> findAllByActiveTrue();

    /** Существует ли сотрудник с таким email */
    boolean existsByEmail(String email);
}