package ru.otus.dutyschedule.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.dutyschedule.dto.request.DepartmentRequest;
import ru.otus.dutyschedule.dto.response.DepartmentResponse;
import ru.otus.dutyschedule.service.DepartmentService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class DepartmentCommands {

    private final DepartmentService departmentService;

    @ShellMethod(key = "department-create", value = "Создать новый отдел")
    public String createDepartment(
            @ShellOption(help = "Название отдела") String name,
            @ShellOption(help = "Особый отдел? (true/false)", defaultValue = "false") boolean special) {
        DepartmentRequest request = DepartmentRequest.builder()
                .name(name)
                .special(special)
                .build();
        DepartmentResponse response = departmentService.create(request);
        return "Отдел создан: " + response.getName() + " (id=" + response.getId() + ")";
    }

    @ShellMethod(key = "department-list", value = "Список всех активных отделов")
    public String listDepartments() {
        List<DepartmentResponse> departments = departmentService.getAllActive();
        if (departments.isEmpty()) {
            return "Нет активных отделов";
        }
        StringBuilder sb = new StringBuilder("Отделы:\n");
        for (DepartmentResponse d : departments) {
            sb.append(String.format("  [%d] %s %s%n",
                    d.getId(), d.getName(), d.isSpecial() ? "(особый)" : ""));
        }
        return sb.toString();
    }

    @ShellMethod(key = "department-special", value = "Сделать отдел особым или обычным")
    public String toggleSpecial(
            @ShellOption(help = "ID отдела") Long id,
            @ShellOption(help = "true - особый, false - обычный") boolean special) {
        DepartmentResponse response = departmentService.toggleSpecial(id, special);
        return "Отдел '" + response.getName() + "' теперь " + (special ? "особый" : "обычный");
    }
}