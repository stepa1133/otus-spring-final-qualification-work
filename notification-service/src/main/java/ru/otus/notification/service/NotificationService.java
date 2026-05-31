package ru.otus.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.notification.dto.NotificationResponse;
import ru.otus.notification.dto.SendNotificationRequest;
import ru.otus.notification.mapper.NotificationMapper;
import ru.otus.notification.model.Notification;
import ru.otus.notification.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    @Transactional
    public NotificationResponse send(SendNotificationRequest request) {
        Notification notification = Notification.builder()
                .employeeId(request.getEmployeeId())
                .employeeEmail(request.getEmployeeEmail())
                .employeeFullName(request.getEmployeeFullName())
                .message(request.getMessage())
                .type(request.getType())
                .sent(false)
                .build();

        notification = repository.save(notification);

        // Имитация отправки
        sendNotification(notification);

        return mapper.toResponse(notification);
    }

    public List<NotificationResponse> getByEmployee(Long employeeId) {
        return repository.findAllByEmployeeId(employeeId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    private void sendNotification(Notification notification) {

        log.info("   Уведомление для {} ({})", notification.getEmployeeFullName(), notification.getEmployeeEmail());
        log.info("   Тип: {}", notification.getType());
        log.info("   Сообщение: {}", notification.getMessage());

        notification.setSent(true);
        notification.setSentAt(LocalDateTime.now());
        repository.save(notification);
    }
}