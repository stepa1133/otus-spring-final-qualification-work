package ru.otus.notification.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long employeeId;

    @Column(nullable = false)
    private String employeeEmail;

    @Column(nullable = false)
    private String employeeFullName;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String type; // DUTY_ASSIGNED, DUTY_CHANGED, DUTY_CANCELLED

    @Column(nullable = false)
    private boolean sent;

    private LocalDateTime sentAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}