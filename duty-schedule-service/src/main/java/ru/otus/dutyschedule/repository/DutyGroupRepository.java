package ru.otus.dutyschedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.dutyschedule.enums.DutyGroupStatus;
import ru.otus.dutyschedule.model.DutyGroup;

import java.util.List;

public interface DutyGroupRepository extends JpaRepository<DutyGroup, Long> {

    /** Все группы с определённым статусом */
    List<DutyGroup> findAllByStatus(DutyGroupStatus status);

    /** Активные группы (черновик или действует), отсортированные по дате */
    List<DutyGroup> findAllByStatusInOrderByStartDateDesc(List<DutyGroupStatus> statuses);
}