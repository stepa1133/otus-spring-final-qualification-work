package ru.otus.dutyschedule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.dutyschedule.client.SecurityFeignClient;
import ru.otus.dutyschedule.dto.request.EmployeeRequest;
import ru.otus.dutyschedule.dto.response.EmployeeResponse;
import ru.otus.dutyschedule.service.EmployeeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API для управления сотрудниками.
 */
@Slf4j
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    private final SecurityFeignClient securityClient;

    /** Зарегистрировать нового сотрудника */
    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest request,
                                                   @RequestHeader("Authorization") String authHeader) {
        EmployeeResponse response = employeeService.create(request);

        // Регистрируем в security-service
        try {
            Map<String, String> securityRequest = new HashMap<>();
            securityRequest.put("username", request.getEmail());
            securityRequest.put("password", request.getPassword());
            securityRequest.put("role", "EMPLOYEE");
            securityClient.registerInSecurityService(securityRequest, authHeader);
        } catch (Exception e) {

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** Получить сотрудника по ID */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    /** Все активные сотрудники */
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllActive() {
        return ResponseEntity.ok(employeeService.getAllActive());
    }

    /** Сотрудники конкретного отдела */
    @GetMapping("/by-department/{departmentId}")
    public ResponseEntity<List<EmployeeResponse>> getByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.getByDepartment(departmentId));
    }

    /** Уволить сотрудника (мягкое удаление) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        employeeService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}