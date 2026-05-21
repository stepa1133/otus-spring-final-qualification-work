package ru.otus.dutyschedule.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.dutyschedule.enums.AbsenceType;

import java.time.LocalDate;

/**
 * Запрос на добавление отсутствия сотрудника.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbsenceRequest {

    @NotNull(message = "ID сотрудника обязателен")
    private Long employeeId;

    @NotNull(message = "Тип отсутствия обязателен")
    private AbsenceType type;

    @NotNull(message = "Дата начала обязательна")
    private LocalDate startDate;

    @NotNull(message = "Дата окончания обязательна")
    private LocalDate endDate;

    private String reason;
}