package ru.otus.security.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String role; // ADMIN, CHIEF, EMPLOYEE
}