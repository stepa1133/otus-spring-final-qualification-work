package ru.otus.dutyschedule.enums;

/**
 * Статус конкретного дежурства.
 * SCHEDULED    — назначено по графику
 * SUBSTITUTED  — перенесено на другого сотрудника
 * CANCELLED    — отменено (например, праздничный день)
 */
public enum DutyStatus {
    SCHEDULED,
    SUBSTITUTED,
    CANCELLED
}