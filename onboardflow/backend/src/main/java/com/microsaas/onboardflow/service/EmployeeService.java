package com.microsaas.onboardflow.service;

import com.microsaas.onboardflow.dto.EmployeeRequest;
import com.microsaas.onboardflow.model.Employee;
import com.microsaas.onboardflow.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;

    @Transactional(readOnly = true)
    public List<Employee> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public Employee findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Transactional
    public Employee create(UUID tenantId, EmployeeRequest request) {
        Employee entity = Employee.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .metadataJson(request.getMetadataJson())
                .build();
        return repository.save(entity);
    }

    @Transactional
    public Employee update(UUID id, UUID tenantId, EmployeeRequest request) {
        Employee entity = findById(id, tenantId);
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getMetadataJson() != null) entity.setMetadataJson(request.getMetadataJson());
        return repository.save(entity);
    }

    public void validate(UUID id, UUID tenantId) {
        Employee entity = findById(id, tenantId);
        // Add business validation logic here
    }
}
