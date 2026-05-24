package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.db.entity.User;
import ru.otus.rest.dto.JwtResponse;
import ru.otus.rest.dto.RegistrationRequest;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final JwtService jwtService;

    public JwtResponse register(RegistrationRequest request) {
        User user = userService.createUser(request);
        var jwt = jwtService.generateToken(user);
        return new JwtResponse(jwt);
    }
}
