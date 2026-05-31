package ru.otus.dutyschedule.mapper;

import org.springframework.stereotype.Component;
import ru.otus.dutyschedule.dto.response.AbsenceResponse;
import ru.otus.dutyschedule.model.Absence;

@Component
public class AbsenceMapper {

    public AbsenceResponse toResponse(Absence absence) {
        return AbsenceResponse.builder()
                .id(absence.getId())
                .employeeFullName(absence.getEmployee() != null
                        ? absence.getEmployee().getFullName()
                        : null)
                .type(absence.getType().name())
                .startDate(absence.getStartDate())
                .endDate(absence.getEndDate())
                .reason(absence.getReason())
                .build();
    }
}