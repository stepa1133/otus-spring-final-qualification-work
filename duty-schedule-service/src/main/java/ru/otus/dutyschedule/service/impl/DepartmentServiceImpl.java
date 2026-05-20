package ru.otus.dutyschedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dutyschedule.dto.request.DepartmentRequest;
import ru.otus.dutyschedule.dto.response.DepartmentResponse;
import ru.otus.dutyschedule.exception.DepartmentNotFoundException;
import ru.otus.dutyschedule.mapper.DepartmentMapper;
import ru.otus.dutyschedule.model.Department;
import ru.otus.dutyschedule.repository.DepartmentRepository;
import ru.otus.dutyschedule.service.DepartmentService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {
        Department department = departmentMapper.toEntity(request);
        Department saved = departmentRepository.save(department);
        return departmentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department department = findEntityById(id);
        departmentMapper.updateEntity(department, request);
        Department saved = departmentRepository.save(department);
        return departmentMapper.toResponse(saved);
    }

    @Override
    public DepartmentResponse getById(Long id) {
        return departmentMapper.toResponse(findEntityById(id));
    }

    @Override
    public List<DepartmentResponse> getAllActive() {
        return departmentRepository.findAllByActiveTrue()
                .stream()
                .map(departmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<DepartmentResponse> getAllSpecial() {
        return departmentRepository.findAllBySpecialTrueAndActiveTrue()
                .stream()
                .map(departmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public DepartmentResponse toggleSpecial(Long id, boolean special) {
        Department department = findEntityById(id);
        department.setSpecial(special);
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Department department = findEntityById(id);
        department.setActive(false);
        departmentRepository.save(department);
    }

    @Override
    public Department findEntityById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
    }
}