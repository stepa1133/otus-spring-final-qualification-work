package ru.otus.rest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.rest.dto.JwtResponse;
import ru.otus.rest.dto.LoginRequest;
import ru.otus.rest.dto.RegistrationRequest;
import ru.otus.service.AuthenticationService;
import ru.otus.service.RegistrationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody @Valid RegistrationRequest request) {
        registrationService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest request) {
        JwtResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}
