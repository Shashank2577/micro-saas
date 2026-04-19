package com.microsaas.compbenchmark.services;

import com.crosscutting.tenancy.TenantContext;
import com.crosscutting.event.EventPublisher;
import com.microsaas.compbenchmark.model.AdjustmentPlan;
import com.microsaas.compbenchmark.repositories.AdjustmentPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdjustmentPlanService {
    private final AdjustmentPlanRepository repository;
    private final EventPublisher eventPublisher;

    public AdjustmentPlan create(AdjustmentPlan entity) {
        UUID tenantId = TenantContext.require();
        entity.setId(UUID.randomUUID());
        entity.setTenantId(tenantId);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        if (entity.getStatus() == null) entity.setStatus("DRAFT");

        AdjustmentPlan saved = repository.save(entity);
        eventPublisher.publish("compbenchmark.plan.recommended", Map.of("id", saved.getId(), "name", saved.getName()));
        return saved;
    }

    public List<AdjustmentPlan> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    public AdjustmentPlan getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    public AdjustmentPlan update(UUID id, AdjustmentPlan updates) {
        AdjustmentPlan existing = getById(id);
        if (updates.getName() != null) existing.setName(updates.getName());
        if (updates.getStatus() != null) existing.setStatus(updates.getStatus());
        if (updates.getMetadataJson() != null) existing.setMetadataJson(updates.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());

        AdjustmentPlan saved = repository.save(existing);
        eventPublisher.publish("compbenchmark.plan.recommended", Map.of("id", saved.getId(), "name", saved.getName()));
        return saved;
    }

    public void delete(UUID id) {
        AdjustmentPlan existing = getById(id);
        repository.delete(existing);
    }

    public Map<String, Object> validate(UUID id) {
        getById(id);
        return Map.of("valid", true, "errors", List.of());
    }

    public Map<String, Object> simulate(UUID id) {
        getById(id);
        return Map.of("result", Map.of("simulatedCoverage", "85%"));
    }
}
