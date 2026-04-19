package com.microsaas.performancenarrative.service;

import com.microsaas.performancenarrative.entity.NarrativeDraft;
import com.microsaas.performancenarrative.repository.NarrativeDraftRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class NarrativeDraftService {

    private final NarrativeDraftRepository repository;

    public NarrativeDraftService(NarrativeDraftRepository repository) {
        this.repository = repository;
    }

    public List<NarrativeDraft> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    public Optional<NarrativeDraft> getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require());
    }

    public NarrativeDraft create(NarrativeDraft entity) {
        entity.setId(UUID.randomUUID());
        entity.setTenantId(TenantContext.require());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        if (entity.getStatus() == null) entity.setStatus("DRAFT");
        return repository.save(entity);
    }

    public NarrativeDraft update(UUID id, NarrativeDraft details) {
        NarrativeDraft existing = repository.findByIdAndTenantId(id, TenantContext.require())
            .orElseThrow(() -> new RuntimeException("Not found"));
        existing.setName(details.getName());
        existing.setStatus(details.getStatus());
        existing.setMetadataJson(details.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    public void delete(UUID id) {
        NarrativeDraft existing = repository.findByIdAndTenantId(id, TenantContext.require())
            .orElseThrow(() -> new RuntimeException("Not found"));
        repository.delete(existing);
    }

    public boolean validate(UUID id) {
        return getById(id).isPresent();
    }

    public String simulate(UUID id) {
        return "Simulated result for " + id;
    }
}
