package ru.otus.dutyschedule.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.dutyschedule.dto.request.EmployeeRequest;
import ru.otus.dutyschedule.dto.response.EmployeeResponse;
import ru.otus.dutyschedule.service.EmployeeService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class EmployeeCommands {

    private final EmployeeService employeeService;

    @ShellMethod(key = "employee-create", value = "Добавить нового сотрудника")
    public String createEmployee(
            @ShellOption(help = "ФИО") String fullName,
            @ShellOption(help = "Email") String email,
            @ShellOption(help = "Пароль") String password,
            @ShellOption(help = "ID отдела") Long departmentId) {
        EmployeeRequest request = EmployeeRequest.builder()
                .fullName(fullName)
                .email(email)
                .password(password)
                .departmentId(departmentId)
                .build();
        EmployeeResponse response = employeeService.create(request);
        return "Сотрудник создан: " + response.getFullName() + " (id=" + response.getId() + ")";
    }

    @ShellMethod(key = "employee-list", value = "Список всех активных сотрудников")
    public String listEmployees() {
        List<EmployeeResponse> employees = employeeService.getAllActive();
        if (employees.isEmpty()) {
            return "Нет активных сотрудников";
        }
        StringBuilder sb = new StringBuilder("Сотрудники:\n");
        for (EmployeeResponse e : employees) {
            sb.append(String.format("  [%d] %s | %s | %s%n",
                    e.getId(), e.getFullName(), e.getDepartmentName(), e.getRole()));
        }
        return sb.toString();
    }

    @ShellMethod(key = "employee-by-department", value = "Сотрудники отдела")
    public String listByDepartment(@ShellOption(help = "ID отдела") Long departmentId) {
        List<EmployeeResponse> employees = employeeService.getByDepartment(departmentId);
        if (employees.isEmpty()) {
            return "В этом отделе нет активных сотрудников";
        }
        StringBuilder sb = new StringBuilder("Сотрудники отдела:\n");
        for (EmployeeResponse e : employees) {
            sb.append(String.format("  [%d] %s%n", e.getId(), e.getFullName()));
        }
        return sb.toString();
    }
}