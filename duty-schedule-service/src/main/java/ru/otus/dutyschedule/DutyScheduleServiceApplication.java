package ru.otus.dutyschedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients

public class DutyScheduleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DutyScheduleServiceApplication.class, args);
	}

}
