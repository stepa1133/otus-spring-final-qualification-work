package ru.otus.dutyschedule.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.dutyschedule.dto.notification.NotificationRequest;

@FeignClient(name = "notification-service", url = "${notification.service.url:http://localhost:8082}")
public interface NotificationFeignClient {

    @PostMapping("/api/notifications")
    void sendNotification(@RequestBody NotificationRequest request);
}