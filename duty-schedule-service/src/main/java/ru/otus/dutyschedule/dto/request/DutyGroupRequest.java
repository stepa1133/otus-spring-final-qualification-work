package ru.otus.dutyschedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Запрос на создание группы дежурств (графика на период).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DutyGroupRequest {

    @NotBlank(message = "Название графика обязательно")
    private String name;

    @NotNull(message = "Дата начала обязательна")
    private LocalDate startDate;

    @NotNull(message = "Дата окончания обязательна")
    private LocalDate endDate;
}