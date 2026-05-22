package ru.otus.dutyschedule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.dutyschedule.dto.request.AbsenceRequest;
import ru.otus.dutyschedule.dto.response.AbsenceResponse;
import ru.otus.dutyschedule.mapper.AbsenceMapper;
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
    private final AbsenceMapper absenceMapper;

    /** Зарегистрировать отсутствие (больничный/отпуск/отгул) */
    @PostMapping
    public ResponseEntity<AbsenceResponse> create(@Valid @RequestBody AbsenceRequest request) {
        Absence absence = absenceService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(absenceMapper.toResponse(absence));
    }

    /** Получить все отсутствия сотрудника */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AbsenceResponse>> getByEmployee(@PathVariable Long employeeId) {
        List<AbsenceResponse> absences = absenceService.getByEmployee(employeeId)
                .stream()
                .map(absenceMapper::toResponse)
                .toList();
        return ResponseEntity.ok(absences);
    }
}