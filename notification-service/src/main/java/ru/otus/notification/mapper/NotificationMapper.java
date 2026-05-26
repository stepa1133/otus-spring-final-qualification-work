package ru.otus.notification.mapper;

import org.springframework.stereotype.Component;
import ru.otus.notification.dto.NotificationResponse;
import ru.otus.notification.model.Notification;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .employeeId(notification.getEmployeeId())
                .employeeFullName(notification.getEmployeeFullName())
                .message(notification.getMessage())
                .type(notification.getType())
                .sent(notification.isSent())
                .sentAt(notification.getSentAt())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}