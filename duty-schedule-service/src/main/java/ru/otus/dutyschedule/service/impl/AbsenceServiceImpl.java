package ru.otus.dutyschedule.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dutyschedule.dto.request.AbsenceRequest;
import ru.otus.dutyschedule.exception.EmployeeNotFoundException;
import ru.otus.dutyschedule.model.Absence;
import ru.otus.dutyschedule.model.DutyGroup;
import ru.otus.dutyschedule.model.Employee;
import ru.otus.dutyschedule.repository.AbsenceRepository;
import ru.otus.dutyschedule.repository.DutyGroupRepository;
import ru.otus.dutyschedule.repository.EmployeeRepository;
import ru.otus.dutyschedule.service.AbsenceService;
import ru.otus.dutyschedule.service.DutyScheduleGenerator;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final EmployeeRepository employeeRepository;
    private final DutyGroupRepository dutyGroupRepository;
    private final DutyScheduleGenerator dutyScheduleGenerator;

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

        Absence saved = absenceRepository.save(absence);

        // Автоматически перегенерировать все активные графики,
        // которые пересекаются с датами отсутствия
        rescheduleAffectedGroups(request.getStartDate(), request.getEndDate());

        return saved;
    }

    /**
     * Находит все активные графики, которые затрагивают период отсутствия,
     * и перегенерирует их.
     */
    private void rescheduleAffectedGroups(LocalDate from, LocalDate to) {
        List<DutyGroup> activeGroups = dutyGroupRepository.findAllByStatus(
                ru.otus.dutyschedule.enums.DutyGroupStatus.ACTIVE);

        for (DutyGroup group : activeGroups) {
            // Проверяем, пересекается ли график с датами отсутствия
            if (!group.getEndDate().isBefore(from) && !group.getStartDate().isAfter(to)) {
                log.info("Автоматическая перегенерация графика '{}' из-за отсутствия сотрудника",
                        group.getName());

                // Удаляем старые дежурства
                List<ru.otus.dutyschedule.model.Duty> oldDuties =
                        dutyGroupRepository.findById(group.getId()).get().getDuties();
                oldDuties.clear();

                // Генерируем новые
                List<ru.otus.dutyschedule.model.Duty> newDuties =
                        dutyScheduleGenerator.generate(group);
                group.getDuties().addAll(newDuties);
                dutyGroupRepository.save(group);

                log.info("График '{}' перегенерирован: {} дежурств",
                        group.getName(), newDuties.size());
            }
        }
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