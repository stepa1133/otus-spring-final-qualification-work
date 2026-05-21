package ru.otus.dutyschedule.model;


import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.dutyschedule.enums.DutyGroupStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Группа дежурств — график на определённый период (обычно месяц).
 * Содержит список конкретных дежурств (Duty) на каждый день периода.
 */
@Entity
@Table(name = "duty_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DutyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Название графика, например "Март 2026" */
    @Column(nullable = false)
    private String name;

    /** Первый день графика */
    @Column(nullable = false)
    private LocalDate startDate;

    /** Последний день графика */
    @Column(nullable = false)
    private LocalDate endDate;

    /** Статус: черновик / действует / завершён */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DutyGroupStatus status = DutyGroupStatus.DRAFT;

    /** Кто создал этот график (начальник) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Employee createdBy;

    /** Все дежурства в этом графике (на каждый день по несколько записей) */
    @OneToMany(mappedBy = "dutyGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Duty> duties = new ArrayList<>();
}