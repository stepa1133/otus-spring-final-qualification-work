package ru.otus.dutyschedule.mapper;

import org.springframework.stereotype.Component;
import ru.otus.dutyschedule.dto.response.DutyResponse;
import ru.otus.dutyschedule.model.Duty;

/**
 * Конвертация Duty → DTO.
 */
@Component
public class DutyMapper {

    /** Дежурство в ответ */
    public DutyResponse toResponse(Duty duty) {
        return DutyResponse.builder()
                .id(duty.getId())
                .date(duty.getDate())
                .employeeFullName(duty.getEmployee() != null
                        ? duty.getEmployee().getFullName()
                        : null)
                .departmentName(duty.getDepartment() != null
                        ? duty.getDepartment().getName()
                        : null)
                .specialDuty(duty.isSpecialDuty())
                .status(duty.getStatus())
                .comment(duty.getComment())
                .substitutedByFullName(duty.getSubstitutedBy() != null
                        ? duty.getSubstitutedBy().getFullName()
                        : null)
                .build();
    }
}