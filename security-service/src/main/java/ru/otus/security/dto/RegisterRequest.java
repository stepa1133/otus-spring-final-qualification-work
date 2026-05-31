package ru.otus.security.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    private final String username;

    private final String password;

    private final String role; // ADMIN, CHIEF, EMPLOYEE
}