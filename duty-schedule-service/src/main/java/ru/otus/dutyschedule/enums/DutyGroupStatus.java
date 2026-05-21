package ru.otus.dutyschedule.enums;


/**
 * Статус группы дежурств (графика на месяц).
 * DRAFT     — черновик, ещё не утверждён, можно редактировать
 * ACTIVE    — утверждён и действует
 * COMPLETED — месяц прошёл, график в архиве
 */
public enum DutyGroupStatus {
    DRAFT,
    ACTIVE,
    COMPLETED
}