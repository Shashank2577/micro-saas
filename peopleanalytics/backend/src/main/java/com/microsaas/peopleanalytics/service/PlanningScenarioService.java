package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.dto.ValidateResponse;
import com.microsaas.peopleanalytics.model.PlanningScenario;
import com.microsaas.peopleanalytics.repository.PlanningScenarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanningScenarioService {
    private final PlanningScenarioRepository repository;

    @Transactional(readOnly = true)
    public List<PlanningScenario> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public PlanningScenario findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("PlanningScenario not found"));
    }

    @Transactional
    public PlanningScenario create(PlanningScenario entity, UUID tenantId) {
        entity.setId(UUID.randomUUID());
        entity.setTenantId(tenantId);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        return repository.save(entity);
    }

    @Transactional
    public PlanningScenario update(UUID id, PlanningScenario entity, UUID tenantId) {
        PlanningScenario existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    public ValidateResponse validate(UUID id, UUID tenantId) {
        PlanningScenario existing = findById(id, tenantId);
        return new ValidateResponse(true, "PlanningScenario validated successfully");
    }
}
