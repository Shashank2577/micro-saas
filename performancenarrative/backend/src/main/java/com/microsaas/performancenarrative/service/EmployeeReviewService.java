package com.microsaas.performancenarrative.service;

import com.microsaas.performancenarrative.entity.EmployeeReview;
import com.microsaas.performancenarrative.repository.EmployeeReviewRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;
import java.util.UUID;
import java.util.List;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class EmployeeReviewService {

    private final EmployeeReviewRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public EmployeeReviewService(EmployeeReviewRepository repository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public List<EmployeeReview> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    public Optional<EmployeeReview> getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require());
    }

    public EmployeeReview create(EmployeeReview entity) {
        entity.setId(UUID.randomUUID());
        entity.setTenantId(TenantContext.require());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        if (entity.getStatus() == null) entity.setStatus("DRAFT");

        EmployeeReview saved = repository.save(entity);
        eventPublisher.publishEvent(new ReviewDraftedEvent(saved.getId(), saved.getTenantId()));
        return saved;
    }

    public EmployeeReview update(UUID id, EmployeeReview details) {
        EmployeeReview existing = repository.findByIdAndTenantId(id, TenantContext.require())
            .orElseThrow(() -> new RuntimeException("Not found"));
        existing.setName(details.getName());
        existing.setStatus(details.getStatus());
        existing.setMetadataJson(details.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());

        if ("FINAL".equals(details.getStatus()) && !"FINAL".equals(existing.getStatus())) {
            eventPublisher.publishEvent(new ReviewFinalizedEvent(existing.getId(), existing.getTenantId()));
        }

        return repository.save(existing);
    }

    public void delete(UUID id) {
        EmployeeReview existing = repository.findByIdAndTenantId(id, TenantContext.require())
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
