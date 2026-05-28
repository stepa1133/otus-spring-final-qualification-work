package ru.otus.security.service;

import ru.otus.security.dto.AuthResponse;
import ru.otus.security.dto.LoginRequest;
import ru.otus.security.dto.RegisterRequest;

public interface AuthService {

    /**
     * Регистрация нового пользователя
     * @param request данные для регистрации (username, password, role)
     * @return AuthResponse с JWT токеном
     * @throws IllegalArgumentException если username уже существует или роль некорректна
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Аутентификация пользователя
     * @param request данные для входа (username, password)
     * @return AuthResponse с JWT токеном
     * @throws IllegalArgumentException если пользователь не найден или пароль неверный
     */
    AuthResponse login(LoginRequest request);
}