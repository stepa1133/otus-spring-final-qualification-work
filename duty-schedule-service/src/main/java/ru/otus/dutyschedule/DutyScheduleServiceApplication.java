package ru.otus.dutyschedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.dutyschedule.config.JwtPublicKeyProperties;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(JwtPublicKeyProperties.class)
public class DutyScheduleServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(DutyScheduleServiceApplication.class, args);
	}
}
