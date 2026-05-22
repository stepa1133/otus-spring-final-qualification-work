package ru.otus.dutyschedule.service;

import ru.otus.dutyschedule.dto.request.DutyGroupRequest;
import ru.otus.dutyschedule.dto.response.DutyGroupResponse;
import ru.otus.dutyschedule.model.DutyGroup;

import java.util.List;

public interface DutyGroupService {

    /** Создать новую группу (черновик) */
    DutyGroupResponse create(DutyGroupRequest request, Long chiefId);

    /** Сгенерировать график для группы */
    DutyGroupResponse generateSchedule(Long groupId);

    /** Перегенерировать график (с учётом отсутствий) */
    DutyGroupResponse reschedule(Long groupId);

    /** Получить группу со всеми дежурствами */
    DutyGroupResponse getById(Long id);

    /** Все группы */
    List<DutyGroupResponse> getAllActive();

    /** Удалить график */
    void delete(Long id);

    /** Найти сущность по ID */
    DutyGroup findEntityById(Long id);
}