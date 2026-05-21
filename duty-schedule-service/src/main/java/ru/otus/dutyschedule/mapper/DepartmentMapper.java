package ru.otus.dutyschedule.mapper;

import org.springframework.stereotype.Component;
import ru.otus.dutyschedule.dto.request.DepartmentRequest;
import ru.otus.dutyschedule.dto.response.DepartmentResponse;
import ru.otus.dutyschedule.model.Department;

/**
 * Конвертация Department ←→ DTO.
 */
@Component
public class DepartmentMapper {

    /** Из запроса в сущность (создание нового отдела) */
    public Department toEntity(DepartmentRequest request) {
        return Department.builder()
                .name(request.getName())
                .special(request.isSpecial())
                .active(true)
                .build();
    }

    /** Из сущности в ответ */
    public DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .special(department.isSpecial())
                .active(department.isActive())
                .chiefFullName(department.getChief() != null
                        ? department.getChief().getFullName()
                        : null)
                .build();
    }

    /** Обновление существующего отдела из запроса */
    public void updateEntity(Department department, DepartmentRequest request) {
        department.setName(request.getName());
        department.setSpecial(request.isSpecial());
    }
}