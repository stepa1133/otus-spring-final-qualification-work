package ru.otus.dutyschedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.dutyschedule.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /** Найти отдел по названию */
    Optional<Department> findByName(String name);

    /** Все активные отделы */
    List<Department> findAllByActiveTrue();

    /** Только особые активные отделы */
    List<Department> findAllBySpecialTrueAndActiveTrue();

    /** Обычные активные отделы (не особые) */
    List<Department> findAllBySpecialFalseAndActiveTrue();
}