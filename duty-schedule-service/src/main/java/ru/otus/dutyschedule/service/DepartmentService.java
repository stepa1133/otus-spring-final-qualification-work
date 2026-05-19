package ru.otus.dutyschedule.service;

import ru.otus.dutyschedule.dto.request.DepartmentRequest;
import ru.otus.dutyschedule.dto.response.DepartmentResponse;
import ru.otus.dutyschedule.model.Department;

import java.util.List;

public interface DepartmentService {

    /** Создать новый отдел */
    DepartmentResponse create(DepartmentRequest request);

    /** Обновить отдел */
    DepartmentResponse update(Long id, DepartmentRequest request);

    /** Получить отдел по ID */
    DepartmentResponse getById(Long id);

    /** Все активные отделы */
    List<DepartmentResponse> getAllActive();

    /** Все особые отделы */
    List<DepartmentResponse> getAllSpecial();

    /** Сделать отдел особым / обычным */
    DepartmentResponse toggleSpecial(Long id, boolean special);

    /** Мягкое удаление (деактивация) */
    void deactivate(Long id);

    /** Найти сущность по ID (для внутреннего использования) */
    Department findEntityById(Long id);
}