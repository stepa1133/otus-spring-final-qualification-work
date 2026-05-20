package ru.otus.dutyschedule.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.dutyschedule.dto.request.DutyGroupRequest;
import ru.otus.dutyschedule.dto.response.DutyGroupResponse;
import ru.otus.dutyschedule.dto.response.DutyResponse;
import ru.otus.dutyschedule.enums.DutyGroupStatus;
import ru.otus.dutyschedule.model.DutyGroup;
import ru.otus.dutyschedule.model.Employee;

import java.util.List;

/**
 * Конвертация DutyGroup ←→ DTO.
 */
@Component
@RequiredArgsConstructor
public class DutyGroupMapper {

    private final DutyMapper dutyMapper;

    /** Создание новой группы (статус DRAFT по умолчанию) */
    public DutyGroup toEntity(DutyGroupRequest request, Employee createdBy) {
        return DutyGroup.builder()
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(DutyGroupStatus.DRAFT)
                .createdBy(createdBy)
                .build();
    }

    /** Группа со списком дежурств */
    public DutyGroupResponse toResponse(DutyGroup dutyGroup) {
        List<DutyResponse> dutyResponses = dutyGroup.getDuties()
                .stream()
                .map(dutyMapper::toResponse)
                .toList();

        return DutyGroupResponse.builder()
                .id(dutyGroup.getId())
                .name(dutyGroup.getName())
                .startDate(dutyGroup.getStartDate())
                .endDate(dutyGroup.getEndDate())
                .status(dutyGroup.getStatus())
                .createdByFullName(dutyGroup.getCreatedBy() != null
                        ? dutyGroup.getCreatedBy().getFullName()
                        : null)
                .duties(dutyResponses)
                .build();
    }
}