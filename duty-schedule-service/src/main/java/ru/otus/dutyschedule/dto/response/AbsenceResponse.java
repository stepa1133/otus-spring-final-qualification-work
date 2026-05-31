package ru.otus.dutyschedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbsenceResponse {

    private Long id;
    private String employeeFullName;
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}