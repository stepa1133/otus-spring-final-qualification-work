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
import ru.otus.dutyschedule.enums.Role;

/**
 * Сотрудник — он же пользователь системы.
 * CHIEF  — начальник, управляет графиками.
 * EMPLOYEE — дежурит, смотрит свои дежурства.
 */
@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ФИО сотрудника */
    @Column(nullable = false)
    private String fullName;

    /** Email — он же логин для входа */
    @Column(nullable = false, unique = true)
    private String email;

    /** Пароль (пока храним здесь, позже уйдёт в security-service) */
    @Column(nullable = false)
    private String password;

    /** Роль: начальник или обычный сотрудник */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /** К какому отделу приписан сотрудник */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    /** Работает ли ещё (false = уволен) */
    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}