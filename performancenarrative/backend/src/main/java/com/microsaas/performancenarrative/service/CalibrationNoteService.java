package com.microsaas.performancenarrative.service;

import com.microsaas.performancenarrative.entity.CalibrationNote;
import com.microsaas.performancenarrative.repository.CalibrationNoteRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;
import java.util.UUID;
import java.util.List;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class CalibrationNoteService {

    private final CalibrationNoteRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public CalibrationNoteService(CalibrationNoteRepository repository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public List<CalibrationNote> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    public Optional<CalibrationNote> getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require());
    }

    public CalibrationNote create(CalibrationNote entity) {
        entity.setId(UUID.randomUUID());
        entity.setTenantId(TenantContext.require());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        if (entity.getStatus() == null) entity.setStatus("DRAFT");
        CalibrationNote saved = repository.save(entity);
        eventPublisher.publishEvent(new CalibrationFlaggedEvent(saved.getId(), saved.getTenantId()));
        return saved;
    }

    public CalibrationNote update(UUID id, CalibrationNote details) {
        CalibrationNote existing = repository.findByIdAndTenantId(id, TenantContext.require())
            .orElseThrow(() -> new RuntimeException("Not found"));
        existing.setName(details.getName());
        existing.setStatus(details.getStatus());
        existing.setMetadataJson(details.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    public void delete(UUID id) {
        CalibrationNote existing = repository.findByIdAndTenantId(id, TenantContext.require())
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
