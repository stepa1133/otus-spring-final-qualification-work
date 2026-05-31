package ru.otus.dutyschedule.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.dutyschedule.client.NotificationFeignClient;
import ru.otus.dutyschedule.dto.notification.NotificationRequest;
import ru.otus.dutyschedule.model.Duty;
import ru.otus.dutyschedule.model.Employee;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationGateway {

    private final NotificationFeignClient feignClient;

    @CircuitBreaker(name = "notification", fallbackMethod = "fallbackSend")
    @Retry(name = "notification")
    public void sendDutyAssigned(Duty duty) {
        Employee employee = duty.getEmployee();
        NotificationRequest request = NotificationRequest.builder()
                .employeeId(employee.getId())
                .employeeEmail(employee.getEmail())
                .employeeFullName(employee.getFullName())
                .message(String.format("Вам назначено дежурство на %s (%s отдел, %s)",
                        duty.getDate(),
                        duty.getDepartment().getName(),
                        duty.isSpecialDuty() ? "особый" : "обычный"))
                .type("DUTY_ASSIGNED")
                .build();

        feignClient.sendNotification(request);
        log.debug("Уведомление отправлено для {}", employee.getFullName());
    }

    public void sendDutyNotifications(List<Duty> duties) {
        for (Duty duty : duties) {
            sendDutyAssigned(duty);
        }
        log.info("Отправлено {} уведомлений о дежурствах", duties.size());
    }

    /**
     * Fallback-метод при недоступности notification-service.
     */
    private void fallbackSend(Duty duty, Throwable t) {
        log.error("Не удалось отправить уведомление для {} ({}). Причина: {}",
                duty.getEmployee().getFullName(),
                duty.getEmployee().getEmail(),
                t.getMessage());
        // Можно сохранить в локальную таблицу для повторной отправки позже
    }
}