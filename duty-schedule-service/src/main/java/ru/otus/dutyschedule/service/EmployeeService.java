package ru.otus.dutyschedule.service;

import ru.otus.dutyschedule.dto.request.EmployeeRequest;
import ru.otus.dutyschedule.dto.response.EmployeeResponse;
import ru.otus.dutyschedule.model.Employee;

import java.util.List;

public interface EmployeeService {

    /** Зарегистрировать нового сотрудника */
    EmployeeResponse create(EmployeeRequest request);

    /** Получить по ID */
    EmployeeResponse getById(Long id);

    /** Все активные сотрудники */
    List<EmployeeResponse> getAllActive();

    /** Все активные сотрудники отдела */
    List<EmployeeResponse> getByDepartment(Long departmentId);

    /** Мягкое удаление (увольнение) */
    void deactivate(Long id);

    /** Найти сущность по ID (для внутреннего использования) */
    Employee findEntityById(Long id);
}