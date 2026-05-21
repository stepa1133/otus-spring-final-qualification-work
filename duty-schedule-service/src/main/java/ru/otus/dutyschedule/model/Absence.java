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
import ru.otus.dutyschedule.enums.AbsenceType;

import java.time.LocalDate;

/**
 * Отсутствие сотрудника: больничный, отпуск, отгул.
 * Учитывается при генерации и перегенерации графика —
 * сотрудник не может дежурить в дни отсутствия.
 */
@Entity
@Table(name = "absences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Кто отсутствует */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /** Тип: больничный / отпуск / отгул */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AbsenceType type;

    /** Первый день отсутствия */
    @Column(nullable = false)
    private LocalDate startDate;

    /** Последний день отсутствия (включительно) */
    @Column(nullable = false)
    private LocalDate endDate;

    /** Причина (необязательно) */
    private String reason;
}