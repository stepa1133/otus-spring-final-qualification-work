package ru.otus.dutyschedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.dutyschedule.enums.DutyStatus;

import java.time.LocalDate;

/**
 * Ответ с информацией об одном дежурстве.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DutyResponse {

    private Long id;
    private LocalDate date;
    private String employeeFullName;     // кто дежурит
    private String departmentName;       // из какого отдела
    private boolean specialDuty;         // из особого отдела?
    private DutyStatus status;
    private String comment;              // комментарий сотрудника
    private String substitutedByFullName; // кто заменил (если есть)
}