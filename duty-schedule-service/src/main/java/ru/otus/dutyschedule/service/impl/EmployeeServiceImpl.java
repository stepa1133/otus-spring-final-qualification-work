package ru.otus.dutyschedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dutyschedule.dto.request.EmployeeRequest;
import ru.otus.dutyschedule.dto.response.EmployeeResponse;
import ru.otus.dutyschedule.exception.DepartmentNotFoundException;
import ru.otus.dutyschedule.exception.DuplicateEmailException;
import ru.otus.dutyschedule.exception.EmployeeNotFoundException;
import ru.otus.dutyschedule.mapper.EmployeeMapper;
import ru.otus.dutyschedule.model.Department;
import ru.otus.dutyschedule.model.Employee;
import ru.otus.dutyschedule.repository.DepartmentRepository;
import ru.otus.dutyschedule.repository.EmployeeRepository;
import ru.otus.dutyschedule.service.EmployeeService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(request.getDepartmentId()));

        Employee employee = employeeMapper.toEntity(request, department, request.getPassword());
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toResponse(saved);
    }

    @Override
    public EmployeeResponse getById(Long id) {
        return employeeMapper.toResponse(findEntityById(id));
    }

    @Override
    public List<EmployeeResponse> getAllActive() {
        return employeeRepository.findAllByActiveTrue()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Override
    public List<EmployeeResponse> getByDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        return employeeRepository.findAllByDepartmentAndActiveTrue(department)
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Employee employee = findEntityById(id);
        employee.setActive(false);
        employeeRepository.save(employee);
    }

    @Override
    public Employee findEntityById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
}