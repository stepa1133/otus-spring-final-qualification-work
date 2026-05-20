package ru.otus.dutyschedule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.dutyschedule.dto.request.CommentRequest;
import ru.otus.dutyschedule.dto.response.DutyResponse;
import ru.otus.dutyschedule.enums.DutyStatus;
import ru.otus.dutyschedule.exception.DutyGroupNotFoundException;
import ru.otus.dutyschedule.model.Duty;
import ru.otus.dutyschedule.repository.DutyRepository;
import ru.otus.dutyschedule.mapper.DutyMapper;

import java.util.List;

/**
 * REST API для работы с конкретными дежурствами.
 * Сотрудник смотрит свои дежурства, оставляет заявки на перенос.
 */
@RestController
@RequestMapping("/api/duties")
@RequiredArgsConstructor
public class DutyController {

    private final DutyRepository dutyRepository;
    private final DutyMapper dutyMapper;

    /** Мои дежурства (для текущего сотрудника) */
    @GetMapping("/my")
    public ResponseEntity<List<DutyResponse>> getMyDuties(@RequestParam Long employeeId) {
        // В будущем employeeId будет браться из JWT-токена
        List<Duty> duties = dutyRepository.findAllByEmployee(
                new ru.otus.dutyschedule.model.Employee()); // временная заглушка
        // TODO: исправить, когда будет security
        return ResponseEntity.ok(List.of());
    }

    /** Оставить комментарий/заявку на перенос */
    @PutMapping("/{id}/comment")
    public ResponseEntity<DutyResponse> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request) {
        Duty duty = dutyRepository.findById(id)
                .orElseThrow(() -> new DutyGroupNotFoundException(id));
        duty.setComment(request.getComment());
        duty.setStatus(DutyStatus.SUBSTITUTED); // помечаем как требующий замены
        dutyRepository.save(duty);
        return ResponseEntity.ok(dutyMapper.toResponse(duty));
    }
}