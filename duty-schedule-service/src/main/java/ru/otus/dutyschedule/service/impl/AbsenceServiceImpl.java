package ru.otus.dutyschedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dutyschedule.dto.request.AbsenceRequest;
import ru.otus.dutyschedule.exception.EmployeeNotFoundException;
import ru.otus.dutyschedule.model.Absence;
import ru.otus.dutyschedule.model.Employee;
import ru.otus.dutyschedule.repository.AbsenceRepository;
import ru.otus.dutyschedule.repository.EmployeeRepository;
import ru.otus.dutyschedule.service.AbsenceService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public Absence create(AbsenceRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException(request.getEmployeeId()));

        Absence absence = Absence.builder()
                .employee(employee)
                .type(request.getType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .build();

        return absenceRepository.save(absence);
    }

    @Override
    public List<Absence> getByEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        return absenceRepository.findAllByEmployee(employee);
    }

    @Override
    public boolean isAbsent(Employee employee, LocalDate date) {
        return absenceRepository.isAbsentOnDate(employee, date);
    }

    @Override
    public List<Employee> getAbsentEmployees(LocalDate date) {
        return absenceRepository.findAbsentEmployeesOnDate(date);
    }
}