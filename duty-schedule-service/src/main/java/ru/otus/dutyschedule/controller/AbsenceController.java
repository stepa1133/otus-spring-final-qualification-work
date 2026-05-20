package ru.otus.dutyschedule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.dutyschedule.dto.request.AbsenceRequest;
import ru.otus.dutyschedule.model.Absence;
import ru.otus.dutyschedule.service.AbsenceService;

import java.util.List;

/**
 * REST API для управления отсутствиями сотрудников.
 */
@RestController
@RequestMapping("/api/absences")
@RequiredArgsConstructor
public class AbsenceController {

    private final AbsenceService absenceService;

    /** Зарегистрировать отсутствие (больничный/отпуск/отгул) */
    @PostMapping
    public ResponseEntity<Absence> create(@Valid @RequestBody AbsenceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(absenceService.create(request));
    }

    /** Получить все отсутствия сотрудника */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Absence>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(absenceService.getByEmployee(employeeId));
    }
}