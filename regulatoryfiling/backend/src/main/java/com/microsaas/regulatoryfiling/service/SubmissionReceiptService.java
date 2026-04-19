package com.microsaas.regulatoryfiling.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.regulatoryfiling.domain.SubmissionReceipt;
import com.microsaas.regulatoryfiling.repository.SubmissionReceiptRepository;
import com.microsaas.regulatoryfiling.dto.ValidationResult;
import com.microsaas.regulatoryfiling.dto.SimulationResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SubmissionReceiptService {
    private final SubmissionReceiptRepository repository;

    public SubmissionReceiptService(SubmissionReceiptRepository repository) {
        this.repository = repository;
    }

    public SubmissionReceipt create(SubmissionReceipt entity) {
        entity.setId(UUID.randomUUID());
        entity.setTenantId(TenantContext.require());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        return repository.save(entity);
    }

    public SubmissionReceipt update(UUID id, SubmissionReceipt entity) {
        SubmissionReceipt existing = getById(id);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<SubmissionReceipt> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public SubmissionReceipt getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("SubmissionReceipt not found"));
    }

    public void delete(UUID id) {
        SubmissionReceipt existing = getById(id);
        repository.delete(existing);
    }

    public ValidationResult validate(UUID id) {
        SubmissionReceipt existing = getById(id);
        return ValidationResult.builder().valid(true).message("Validated " + existing.getName()).build();
    }

    public SimulationResult simulate(UUID id) {
        SubmissionReceipt existing = getById(id);
        return SimulationResult.builder().success(true).outcome("Simulated " + existing.getName()).build();
    }
}
