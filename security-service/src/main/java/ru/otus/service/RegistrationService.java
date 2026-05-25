package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.rest.dto.RegistrationRequest;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;

    public void register(RegistrationRequest request) {
        userService.createUser(request);
    }
}
