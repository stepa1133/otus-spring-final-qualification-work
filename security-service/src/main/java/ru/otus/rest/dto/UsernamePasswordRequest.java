package ru.otus.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsernamePasswordRequest {

    @Size(min = 4, max = 32, message = "Username must be from 4 to 32 chars")
    @NotBlank(message = "Username is required")
    private String username;

    @Size(min = 4, max = 32, message = "Password must be from 4 to 32 chars")
    @NotBlank(message = "Password is required")
    private String password;
}


