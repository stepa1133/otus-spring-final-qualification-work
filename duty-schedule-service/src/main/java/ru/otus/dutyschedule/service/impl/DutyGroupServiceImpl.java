package ru.otus.dutyschedule.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dutyschedule.dto.request.DutyGroupRequest;
import ru.otus.dutyschedule.dto.response.DutyGroupResponse;
import ru.otus.dutyschedule.enums.DutyGroupStatus;
import ru.otus.dutyschedule.exception.DutyGroupNotFoundException;
import ru.otus.dutyschedule.exception.EmployeeNotFoundException;
import ru.otus.dutyschedule.mapper.DutyGroupMapper;
import ru.otus.dutyschedule.model.Duty;
import ru.otus.dutyschedule.model.DutyGroup;
import ru.otus.dutyschedule.model.Employee;
import ru.otus.dutyschedule.repository.DutyGroupRepository;
import ru.otus.dutyschedule.repository.DutyRepository;
import ru.otus.dutyschedule.repository.EmployeeRepository;
import ru.otus.dutyschedule.service.DutyGroupService;
import ru.otus.dutyschedule.service.DutyScheduleGenerator;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DutyGroupServiceImpl implements DutyGroupService {

    private final DutyGroupRepository dutyGroupRepository;
    private final DutyRepository dutyRepository;
    private final EmployeeRepository employeeRepository;
    private final DutyGroupMapper dutyGroupMapper;
    private final DutyScheduleGenerator dutyScheduleGenerator;

    @Override
    @Transactional
    public DutyGroupResponse create(DutyGroupRequest request, Long chiefId) {
        Employee chief = employeeRepository.findById(chiefId)
                .orElseThrow(() -> new EmployeeNotFoundException(chiefId));

        DutyGroup dutyGroup = dutyGroupMapper.toEntity(request, chief);
        DutyGroup saved = dutyGroupRepository.save(dutyGroup);
        log.info("Создана группа дежурств '{}'", saved.getName());
        return dutyGroupMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public DutyGroupResponse generateSchedule(Long groupId) {
        DutyGroup dutyGroup = findEntityById(groupId);

        // Удаляем старые дежурства (если были)
        List<Duty> oldDuties = dutyRepository.findAllByDutyGroup(dutyGroup);
        dutyRepository.deleteAll(oldDuties);
        dutyGroup.getDuties().clear();

        // Генерируем новые
        List<Duty> duties = dutyScheduleGenerator.generate(dutyGroup);
        dutyGroup.getDuties().addAll(duties);

        dutyGroupRepository.save(dutyGroup);
        log.info("График '{}' сгенерирован, {} дежурств", dutyGroup.getName(), duties.size());
        return dutyGroupMapper.toResponse(dutyGroup);
    }

    @Override
    @Transactional
    public DutyGroupResponse reschedule(Long groupId) {
        DutyGroup dutyGroup = findEntityById(groupId);

        // Перегенерируем на все даты периода
        List<Duty> duties = dutyScheduleGenerator.generate(dutyGroup);

        // Удаляем старые и сохраняем новые
        List<Duty> oldDuties = dutyRepository.findAllByDutyGroup(dutyGroup);
        dutyRepository.deleteAll(oldDuties);
        dutyGroup.getDuties().clear();
        dutyGroup.getDuties().addAll(duties);

        dutyGroupRepository.save(dutyGroup);
        log.info("График '{}' перегенерирован", dutyGroup.getName());
        return dutyGroupMapper.toResponse(dutyGroup);
    }

    @Override
    public DutyGroupResponse getById(Long id) {
        return dutyGroupMapper.toResponse(findEntityById(id));
    }

    @Override
    public List<DutyGroupResponse> getAllActive() {
        List<DutyGroupStatus> activeStatuses = List.of(DutyGroupStatus.DRAFT, DutyGroupStatus.ACTIVE);
        return dutyGroupRepository.findAllByStatusInOrderByStartDateDesc(activeStatuses)
                .stream()
                .map(dutyGroupMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public DutyGroupResponse activate(Long id) {
        DutyGroup dutyGroup = findEntityById(id);
        if (dutyGroup.getDuties().isEmpty()) {
            throw new IllegalStateException("Нельзя активировать график без дежурств. Сначала сгенерируйте.");
        }
        dutyGroup.setStatus(DutyGroupStatus.ACTIVE);
        dutyGroupRepository.save(dutyGroup);
        log.info("График '{}' активирован", dutyGroup.getName());
        return dutyGroupMapper.toResponse(dutyGroup);
    }

    @Override
    public DutyGroup findEntityById(Long id) {
        return dutyGroupRepository.findById(id)
                .orElseThrow(() -> new DutyGroupNotFoundException(id));
    }
}