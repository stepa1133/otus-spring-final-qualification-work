package ru.otus.dutyschedule.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.dutyschedule.exception.NotEnoughEmployeesException;
import ru.otus.dutyschedule.model.Department;
import ru.otus.dutyschedule.model.Duty;
import ru.otus.dutyschedule.model.DutyGroup;
import ru.otus.dutyschedule.model.Employee;
import ru.otus.dutyschedule.repository.DepartmentRepository;
import ru.otus.dutyschedule.repository.DutyRepository;
import ru.otus.dutyschedule.repository.EmployeeRepository;
import ru.otus.dutyschedule.service.AbsenceService;
import ru.otus.dutyschedule.service.DutyScheduleGenerator;

import java.time.LocalDate;
import java.util.*;

/**
 * Реализация алгоритма генерации графика дежурств.
 *
 * Правила:
 * 1. Из каждого особого отдела — ровно 1 дежурный каждый день
 * 2. Из всех обычных отделов вместе — 1 дежурный каждый день
 * 3. Исключаем отсутствующих (больничный, отпуск, отгул)
 * 4. Исключаем уволенных (active = false)
 * 5. Распределяем равномерно: кто реже дежурил — тот и назначается
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DutyScheduleGeneratorImpl implements DutyScheduleGenerator {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final AbsenceService absenceService;
    private final DutyRepository dutyRepository;


    @Override
    public List<Duty> generate(DutyGroup dutyGroup) {
        log.info("Генерация графика '{}' с {} по {}",
                dutyGroup.getName(), dutyGroup.getStartDate(), dutyGroup.getEndDate());

        // 1. Получаем все активные отделы
        List<Department> specialDepartments = departmentRepository.findAllBySpecialTrueAndActiveTrue();
        List<Department> regularDepartments = departmentRepository.findAllBySpecialFalseAndActiveTrue();

        log.info("Особых отделов: {}, обычных: {}", specialDepartments.size(), regularDepartments.size());

        // 2. Для каждого дня периода создаём дежурства
        List<Duty> allDuties = new ArrayList<>();
        LocalDate currentDate = dutyGroup.getStartDate();

        while (!currentDate.isAfter(dutyGroup.getEndDate())) {
            List<Duty> dayDuties = generateDayDuties(
                    currentDate,
                    dutyGroup,
                    specialDepartments,
                    regularDepartments
            );
            allDuties.addAll(dayDuties);
            currentDate = currentDate.plusDays(1);
        }

        log.info("Сгенерировано {} дежурств", allDuties.size());
        return allDuties;
    }

    @Override
    public List<Duty> rescheduleDate(DutyGroup dutyGroup, LocalDate date) {
        log.info("Перегенерация дежурств на дату {}", date);

        List<Department> specialDepartments = departmentRepository.findAllBySpecialTrueAndActiveTrue();
        List<Department> regularDepartments = departmentRepository.findAllBySpecialFalseAndActiveTrue();

        return generateDayDuties(date, dutyGroup, specialDepartments, regularDepartments);
    }

    /**
     * Сгенерировать дежурства на один конкретный день.
     */
    private List<Duty> generateDayDuties(
            LocalDate date,
            DutyGroup dutyGroup,
            List<Department> specialDepartments,
            List<Department> regularDepartments) {

        List<Duty> dayDuties = new ArrayList<>();

        // --- Особые отделы: по одному человеку с каждого ---
        for (Department department : specialDepartments) {
            Employee dutyEmployee = findAvailableEmployee(department, date);
            if (dutyEmployee == null) {
                throw new NotEnoughEmployeesException(department.getName(), date);
            }

            dayDuties.add(Duty.builder()
                    .date(date)
                    .dutyGroup(dutyGroup)
                    .employee(dutyEmployee)
                    .department(department)
                    .specialDuty(true)
                    .status(ru.otus.dutyschedule.enums.DutyStatus.SCHEDULED)
                    .build());
        }

        // --- Обычные отделы: один человек суммарно со всех ---
        if (!regularDepartments.isEmpty()) {
            Employee dutyEmployee = findAvailableFromRegularDepartments(regularDepartments, date);
            if (dutyEmployee == null) {
                throw new NotEnoughEmployeesException(
                        "Нет доступных сотрудников из обычных отделов на " + date);
            }

            dayDuties.add(Duty.builder()
                    .date(date)
                    .dutyGroup(dutyGroup)
                    .employee(dutyEmployee)
                    .department(dutyEmployee.getDepartment())
                    .specialDuty(false)
                    .status(ru.otus.dutyschedule.enums.DutyStatus.SCHEDULED)
                    .build());
        }

        return dayDuties;
    }

    /**
     * Найти доступного сотрудника в конкретном отделе на дату.
     * Выбираем того, кто реже всего дежурил (равномерное распределение).
     */
    private Employee findAvailableEmployee(Department department, LocalDate date) {
        List<Employee> allEmployees = employeeRepository.findAllByDepartmentAndActiveTrue(department);

        // Убираем отсутствующих
        List<Employee> available = allEmployees.stream()
                .filter(emp -> !absenceService.isAbsent(emp, date))
                .toList();

        if (available.isEmpty()) {
            return null;
        }

        // Выбираем того, кто реже всех дежурил (для равномерности)
        return available.stream()
                .min(Comparator.comparingLong(this::countPastDuties))
                .orElse(null);
    }

    /**
     * Найти доступного сотрудника из всех обычных отделов.
     * Выбираем того, кто реже всех дежурил среди всех обычных отделов.
     */
    private Employee findAvailableFromRegularDepartments(List<Department> regularDepartments, LocalDate date) {
        List<Employee> allRegularEmployees = new ArrayList<>();
        for (Department dept : regularDepartments) {
            allRegularEmployees.addAll(employeeRepository.findAllByDepartmentAndActiveTrue(dept));
        }

        List<Employee> available = allRegularEmployees.stream()
                .filter(emp -> !absenceService.isAbsent(emp, date))
                .toList();

        if (available.isEmpty()) {
            return null;
        }

        // Выбираем того, кто реже всех дежурил
        return available.stream()
                .min(Comparator.comparingLong(this::countPastDuties))
                .orElse(null);
    }

    /**
     * Посчитать, сколько всего дежурств было у сотрудника.
     * Чем меньше — тем выше приоритет для нового дежурства.
     */
    private long countPastDuties(Employee employee) {
        // Сколько всего дежурств было у сотрудника
        // Чем меньше — тем выше приоритет
        return dutyRepository.countByEmployee(employee);
    }
}