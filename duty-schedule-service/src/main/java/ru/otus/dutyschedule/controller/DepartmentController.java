package ru.otus.dutyschedule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dutyschedule.dto.request.DepartmentRequest;
import ru.otus.dutyschedule.dto.response.DepartmentResponse;
import ru.otus.dutyschedule.service.DepartmentService;

import java.util.List;

/**
 * REST API для управления отделами.
 * Доступен только начальнику (CHIEF).
 */
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    /** Создать новый отдел */
    @PostMapping
    public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(departmentService.create(request));
    }

    /** Обновить отдел */
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.ok(departmentService.update(id, request));
    }

    /** Получить отдел по ID */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getById(id));
    }

    /** Все активные отделы */
    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllActive() {
        return ResponseEntity.ok(departmentService.getAllActive());
    }

    /** Только особые отделы */
    @GetMapping("/special")
    public ResponseEntity<List<DepartmentResponse>> getAllSpecial() {
        return ResponseEntity.ok(departmentService.getAllSpecial());
    }

    /** Сделать отдел особым или обычным */
    @PatchMapping("/{id}/special")
    public ResponseEntity<DepartmentResponse> toggleSpecial(
            @PathVariable Long id,
            @RequestParam boolean special) {
        return ResponseEntity.ok(departmentService.toggleSpecial(id, special));
    }

    /** Удалить отдел (мягкое удаление) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        departmentService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}