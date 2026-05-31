package ru.otus.dutyschedule.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.Map;

@FeignClient(name = "security-service", url = "${security.service.url:http://localhost:8081}")
public interface SecurityFeignClient {

    @PostMapping("/auth/register")
    void registerInSecurityService(@RequestBody Map<String, String> request,
                                   @RequestHeader("Authorization") String token);
}