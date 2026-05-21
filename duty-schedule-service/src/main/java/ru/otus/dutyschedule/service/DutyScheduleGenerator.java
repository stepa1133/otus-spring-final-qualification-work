package ru.otus.dutyschedule.service;

import ru.otus.dutyschedule.model.Duty;
import ru.otus.dutyschedule.model.DutyGroup;

import java.util.List;

/**
 * Основной алгоритм генерации графика дежурств.
 *
 * Правила:
 * - Из каждого особого отдела — 1 дежурный в день
 * - Из всех обычных отделов вместе — 1 дежурный в день
 * - Сотрудники на больничном/в отпуске — исключаются
 * - Дежурства распределяются равномерно
 */
public interface DutyScheduleGenerator {

    /**
     * Сгенерировать список дежурств на весь период графика.
     *
     * @param dutyGroup группа дежурств (содержит startDate и endDate)
     * @return список дежурств на каждый день периода
     */
    List<Duty> generate(DutyGroup dutyGroup);

    /**
     * Перегенерировать дежурства на определённую дату
     * (например, если сотрудник ушёл на больничный).
     *
     * @param dutyGroup группа дежурств
     * @param date дата, которую нужно перегенерировать
     * @return новые дежурства на эту дату
     */
    List<Duty> rescheduleDate(DutyGroup dutyGroup, java.time.LocalDate date);
}