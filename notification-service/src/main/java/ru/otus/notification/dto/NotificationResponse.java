package ru.otus.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private Long employeeId;
    private String employeeFullName;
    private String message;
    private String type;
    private boolean sent;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
}