package ru.otus.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.notification.dto.NotificationResponse;
import ru.otus.notification.dto.SendNotificationRequest;
import ru.otus.notification.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping
    public ResponseEntity<NotificationResponse> send(@RequestBody SendNotificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.send(request));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<NotificationResponse>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.getByEmployee(employeeId));
    }
}