package ru.otus.dutyschedule.mapper;

import org.springframework.stereotype.Component;
import ru.otus.dutyschedule.dto.request.EmployeeRequest;
import ru.otus.dutyschedule.dto.response.EmployeeResponse;
import ru.otus.dutyschedule.enums.Role;
import ru.otus.dutyschedule.model.Department;
import ru.otus.dutyschedule.model.Employee;

/**
 * Конвертация Employee ←→ DTO.
 */
@Component
public class EmployeeMapper {

    /** Создание нового сотрудника (роль EMPLOYEE по умолчанию) */
    public Employee toEntity(EmployeeRequest request, Department department, String encodedPassword) {
        return Employee.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(encodedPassword)  // пароль уже закодирован
                .role(Role.EMPLOYEE)        // по умолчанию обычный сотрудник
                .department(department)
                .active(true)
                .build();
    }

    /** В ответе НЕ передаём пароль */
    public EmployeeResponse toResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .fullName(employee.getFullName())
                .email(employee.getEmail())
                .role(employee.getRole())
                .departmentName(employee.getDepartment() != null
                        ? employee.getDepartment().getName()
                        : null)
                .active(employee.isActive())
                .build();
    }
}