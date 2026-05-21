package ru.otus.dutyschedule.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.dutyschedule.enums.DutyStatus;

import java.time.LocalDate;

/**
 * Конкретное дежурство — один сотрудник в один день.
 * Если особых отделов 2, а обычных 3, то на один день будет 3 записи Duty:
 *   - 2 записи от особых отделов (по одной на отдел)
 *   - 1 запись от обычных отделов (один человек суммарно)
 */
@Entity
@Table(name = "duties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Duty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Дата дежурства */
    @Column(nullable = false)
    private LocalDate date;

    /** К какому графику относится */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duty_group_id")
    private DutyGroup dutyGroup;

    /** Кто дежурит */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    /** Из какого отдела этот дежурный (для отчётности и фильтрации) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    /** Дежурство из особого отдела? */
    @Column(nullable = false)
    private boolean specialDuty;

    /** Статус: назначено / заменено / отменено */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DutyStatus status = DutyStatus.SCHEDULED;

    /** Комментарий сотрудника (например, просьба перенести) */
    @Column(columnDefinition = "TEXT")
    private String comment;

    /** Кто заменил (если статус SUBSTITUTED) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substituted_by")
    private Employee substitutedBy;
}