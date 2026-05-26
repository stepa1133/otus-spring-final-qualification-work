package ru.otus.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequest {

    private Long employeeId;
    private String employeeEmail;
    private String employeeFullName;
    private String message;
    private String type;
}