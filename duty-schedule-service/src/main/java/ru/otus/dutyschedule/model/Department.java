package ru.otus.dutyschedule.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

/**
 * Отдел / подразделение.
 * special = true — особый отдел (каждый день должен дежурить один человек из этого отдела).
 * special = false — обычный отдел (из всех обычных отделов дежурит суммарно один человек).
 */
@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Название отдела, например "Бухгалтерия", "ИТ", "HR" */
    @Column(nullable = false, unique = true)
    private String name;

    /** Является ли отдел особым (обязательный ежедневный дежурный) */
    @Column(nullable = false)
    private boolean special;

    /** Начальник, который управляет этим отделом (твоя подруга) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chief_id")
    private Employee chief;

    /** Активен ли отдел (можно мягко удалить, не теряя историю) */
    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}