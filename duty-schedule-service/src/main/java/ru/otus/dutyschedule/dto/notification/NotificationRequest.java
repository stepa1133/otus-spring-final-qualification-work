package ru.otus.dutyschedule.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    private Long employeeId;
    private String employeeEmail;
    private String employeeFullName;
    private String message;
    private String type;
}