package ru.otus.dutyschedule.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.dutyschedule.dto.request.DutyGroupRequest;
import ru.otus.dutyschedule.dto.response.DutyGroupResponse;
import ru.otus.dutyschedule.dto.response.DutyResponse;
import ru.otus.dutyschedule.service.DutyGroupService;

import java.time.LocalDate;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class DutyGroupCommands {

    private final DutyGroupService dutyGroupService;

    @ShellMethod(key = "schedule-create", value = "Создать новый график дежурств")
    public String createSchedule(
            @ShellOption(help = "Название графика") String name,
            @ShellOption(help = "Дата начала (ГГГГ-ММ-ДД)") String startDate,
            @ShellOption(help = "Дата окончания (ГГГГ-ММ-ДД)") String endDate,
            @ShellOption(help = "ID начальника") Long chiefId) {
        DutyGroupRequest request = DutyGroupRequest.builder()
                .name(name)
                .startDate(LocalDate.parse(startDate))
                .endDate(LocalDate.parse(endDate))
                .build();
        DutyGroupResponse response = dutyGroupService.create(request, chiefId);
        return "График создан: " + response.getName() + " (id=" + response.getId() + ")";
    }

    @ShellMethod(key = "schedule-generate", value = "Сгенерировать дежурства для графика")
    public String generateSchedule(@ShellOption(help = "ID графика") Long groupId) {
        DutyGroupResponse response = dutyGroupService.generateSchedule(groupId);
        return "График '" + response.getName() + "' сгенерирован! "
                + response.getDuties().size() + " дежурств";
    }

    @ShellMethod(key = "schedule-view", value = "Посмотреть график дежурств")
    public String viewSchedule(@ShellOption(help = "ID графика") Long groupId) {
        DutyGroupResponse response = dutyGroupService.getById(groupId);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== %s ===%n", response.getName()));
        sb.append(String.format("Период: %s — %s%n", response.getStartDate(), response.getEndDate()));
        sb.append(String.format("Статус: %s%n", response.getStatus()));
        sb.append(String.format("Создал: %s%n%n", response.getCreatedByFullName()));

        List<DutyResponse> duties = response.getDuties();
        if (duties.isEmpty()) {
            sb.append("График ещё не сгенерирован. Выполните schedule-generate");
            return sb.toString();
        }

        sb.append("Дата       | Дежурный              | Отдел          | Тип\n");
        sb.append("-----------+-----------------------+----------------+----------\n");
        for (DutyResponse d : duties) {
            sb.append(String.format("%s | %-21s | %-14s | %s%n",
                    d.getDate(),
                    d.getEmployeeFullName(),
                    d.getDepartmentName(),
                    d.isSpecialDuty() ? "Особый" : "Обычный"));
        }
        return sb.toString();
    }

    @ShellMethod(key = "schedule-list", value = "Список всех графиков")
    public String listSchedules() {
        List<DutyGroupResponse> groups = dutyGroupService.getAllActive();
        if (groups.isEmpty()) {
            return "Нет активных графиков";
        }
        StringBuilder sb = new StringBuilder("Графики:\n");
        for (DutyGroupResponse g : groups) {
            sb.append(String.format("  [%d] %s | %s — %s | %s | дежурств: %d%n",
                    g.getId(), g.getName(), g.getStartDate(), g.getEndDate(),
                    g.getStatus(), g.getDuties() != null ? g.getDuties().size() : 0));
        }
        return sb.toString();
    }
}