package ru.otus.dutyschedule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dutyschedule.dto.request.DutyGroupRequest;
import ru.otus.dutyschedule.dto.response.DutyGroupResponse;
import ru.otus.dutyschedule.service.DutyGroupService;

import java.util.List;

/**
 * REST API для управления группами дежурств (графиками).
 * Основные действия начальника: создать группу, сгенерировать, активировать.
 */
@RestController
@RequestMapping("/api/duty-groups")
@RequiredArgsConstructor
public class DutyGroupController {

    private final DutyGroupService dutyGroupService;

    /** Создать новую группу (черновик) */
    @PostMapping
    public ResponseEntity<DutyGroupResponse> create(
            @Valid @RequestBody DutyGroupRequest request,
            @RequestParam Long chiefId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dutyGroupService.create(request, chiefId));
    }

    /** Сгенерировать график для группы */
    @PostMapping("/{id}/generate")
    public ResponseEntity<DutyGroupResponse> generateSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(dutyGroupService.generateSchedule(id));
    }

    /** Перегенерировать график (при изменениях) */
    @PostMapping("/{id}/reschedule")
    public ResponseEntity<DutyGroupResponse> reschedule(@PathVariable Long id) {
        return ResponseEntity.ok(dutyGroupService.reschedule(id));
    }

    /** Получить группу со всеми дежурствами */
    @GetMapping("/{id}")
    public ResponseEntity<DutyGroupResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(dutyGroupService.getById(id));
    }

    /** Все активные группы (черновики + действующие) */
    @GetMapping
    public ResponseEntity<List<DutyGroupResponse>> getAllActive() {
        return ResponseEntity.ok(dutyGroupService.getAllActive());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dutyGroupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}