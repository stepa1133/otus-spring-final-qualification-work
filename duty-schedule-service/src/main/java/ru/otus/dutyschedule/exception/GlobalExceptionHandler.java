package ru.otus.dutyschedule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.dutyschedule.dto.response.ErrorResponse;


import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Единый обработчик всех исключений.
 * Перехватывает ошибки из контроллеров и возвращает понятный JSON.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Отдел не найден → 404 */
    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDepartmentNotFound(DepartmentNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** Сотрудник не найден → 404 */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** График не найден → 404 */
    @ExceptionHandler(DutyGroupNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDutyGroupNotFound(DutyGroupNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** Не хватает сотрудников → 409 Conflict */
    @ExceptionHandler(NotEnoughEmployeesException.class)
    public ResponseEntity<ErrorResponse> handleNotEnoughEmployees(NotEnoughEmployeesException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Дубликат email → 409 Conflict */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Ошибки валидации (@Valid) → 400 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    /** Все остальные ошибки → 500 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Внутренняя ошибка сервера: " + ex.getMessage());
    }

    /** Вспомогательный метод для формирования ответа */
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        ErrorResponse error = ErrorResponse.builder()
                .status(status.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(error);
    }
}