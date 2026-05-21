package ru.otus.dutyschedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Запрос на создание или обновление отдела.
 * special = true — особый отдел (ежедневный обязательный дежурный).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequest {

    @NotBlank(message = "Название отдела обязательно")
    private String name;

    private boolean special;
}