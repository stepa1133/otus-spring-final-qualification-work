package ru.otus.dutyschedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.dutyschedule.enums.Role;

/**
 * Ответ с информацией о сотруднике.
 * Пароль не передаём!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private String departmentName;  // название отдела вместо целого объекта
    private boolean active;
}