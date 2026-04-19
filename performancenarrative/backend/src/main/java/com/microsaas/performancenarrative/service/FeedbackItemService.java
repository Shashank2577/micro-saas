package com.microsaas.performancenarrative.service;

import com.microsaas.performancenarrative.entity.FeedbackItem;
import com.microsaas.performancenarrative.repository.FeedbackItemRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class FeedbackItemService {

    private final FeedbackItemRepository repository;

    public FeedbackItemService(FeedbackItemRepository repository) {
        this.repository = repository;
    }

    public List<FeedbackItem> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    public Optional<FeedbackItem> getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require());
    }

    public FeedbackItem create(FeedbackItem entity) {
        entity.setId(UUID.randomUUID());
        entity.setTenantId(TenantContext.require());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        if (entity.getStatus() == null) entity.setStatus("DRAFT");
        return repository.save(entity);
    }

    public FeedbackItem update(UUID id, FeedbackItem details) {
        FeedbackItem existing = repository.findByIdAndTenantId(id, TenantContext.require())
            .orElseThrow(() -> new RuntimeException("Not found"));
        existing.setName(details.getName());
        existing.setStatus(details.getStatus());
        existing.setMetadataJson(details.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    public void delete(UUID id) {
        FeedbackItem existing = repository.findByIdAndTenantId(id, TenantContext.require())
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
