package ru.otus.dutyschedule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.dutyschedule.dto.request.EmployeeRequest;
import ru.otus.dutyschedule.dto.response.EmployeeResponse;
import ru.otus.dutyschedule.service.EmployeeService;

import java.util.List;

/**
 * REST API для управления сотрудниками.
 */
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /** Зарегистрировать нового сотрудника */
    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.create(request));
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