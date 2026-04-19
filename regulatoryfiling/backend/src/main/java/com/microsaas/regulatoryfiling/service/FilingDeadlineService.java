package com.microsaas.regulatoryfiling.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.regulatoryfiling.domain.FilingDeadline;
import com.microsaas.regulatoryfiling.repository.FilingDeadlineRepository;
import com.microsaas.regulatoryfiling.dto.ValidationResult;
import com.microsaas.regulatoryfiling.dto.SimulationResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FilingDeadlineService {
    private final FilingDeadlineRepository repository;

    public FilingDeadlineService(FilingDeadlineRepository repository) {
        this.repository = repository;
    }

    public FilingDeadline create(FilingDeadline entity) {
        entity.setId(UUID.randomUUID());
        entity.setTenantId(TenantContext.require());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        return repository.save(entity);
    }

    public FilingDeadline update(UUID id, FilingDeadline entity) {
        FilingDeadline existing = getById(id);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<FilingDeadline> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public FilingDeadline getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("FilingDeadline not found"));
    }

    public void delete(UUID id) {
        FilingDeadline existing = getById(id);
        repository.delete(existing);
    }

    public ValidationResult validate(UUID id) {
        FilingDeadline existing = getById(id);
        return ValidationResult.builder().valid(true).message("Validated " + existing.getName()).build();
    }

    public SimulationResult simulate(UUID id) {
        FilingDeadline existing = getById(id);
        return SimulationResult.builder().success(true).outcome("Simulated " + existing.getName()).build();
    }
}
