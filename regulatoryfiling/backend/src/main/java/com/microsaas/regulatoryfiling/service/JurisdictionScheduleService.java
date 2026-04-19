package com.microsaas.regulatoryfiling.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.regulatoryfiling.domain.JurisdictionSchedule;
import com.microsaas.regulatoryfiling.repository.JurisdictionScheduleRepository;
import com.microsaas.regulatoryfiling.dto.ValidationResult;
import com.microsaas.regulatoryfiling.dto.SimulationResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class JurisdictionScheduleService {
    private final JurisdictionScheduleRepository repository;

    public JurisdictionScheduleService(JurisdictionScheduleRepository repository) {
        this.repository = repository;
    }

    public JurisdictionSchedule create(JurisdictionSchedule entity) {
        entity.setId(UUID.randomUUID());
        entity.setTenantId(TenantContext.require());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        return repository.save(entity);
    }

    public JurisdictionSchedule update(UUID id, JurisdictionSchedule entity) {
        JurisdictionSchedule existing = getById(id);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<JurisdictionSchedule> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public JurisdictionSchedule getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("JurisdictionSchedule not found"));
    }

    public void delete(UUID id) {
        JurisdictionSchedule existing = getById(id);
        repository.delete(existing);
    }

    public ValidationResult validate(UUID id) {
        JurisdictionSchedule existing = getById(id);
        return ValidationResult.builder().valid(true).message("Validated " + existing.getName()).build();
    }

    public SimulationResult simulate(UUID id) {
        JurisdictionSchedule existing = getById(id);
        return SimulationResult.builder().success(true).outcome("Simulated " + existing.getName()).build();
    }
}
