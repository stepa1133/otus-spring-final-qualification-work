package ru.otus.dutyschedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.dutyschedule.enums.DutyGroupStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Ответ с информацией о группе дежурств.
 * Содержит список дежурств внутри.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DutyGroupResponse {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private DutyGroupStatus status;
    private String createdByFullName;
    private List<DutyResponse> duties;   // вложенный список дежурств
}