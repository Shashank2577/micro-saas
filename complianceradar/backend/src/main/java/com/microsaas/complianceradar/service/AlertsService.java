package com.microsaas.complianceradar.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.complianceradar.domain.DeadlineAlert;
import com.microsaas.complianceradar.repository.DeadlineAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlertsService {

    private final DeadlineAlertRepository repository;

    @Transactional(readOnly = true)
    public List<DeadlineAlert> list() {
        return repository.findAllByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public DeadlineAlert getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("DeadlineAlert not found"));
    }

    @Transactional
    public DeadlineAlert create(DeadlineAlert alert) {
        alert.setId(UUID.randomUUID());
        alert.setTenantId(TenantContext.require());
        alert.setCreatedAt(Instant.now());
        return repository.save(alert);
    }

    @Transactional
    public DeadlineAlert update(UUID id, DeadlineAlert updateDetails) {
        DeadlineAlert existing = getById(id);
        existing.setName(updateDetails.getName());
        existing.setStatus(updateDetails.getStatus());
        existing.setMetadataJson(updateDetails.getMetadataJson());
        existing.setUpdatedAt(Instant.now());
        return repository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteByIdAndTenantId(id, TenantContext.require());
    }

    @Transactional
    public void validate(UUID id) {
        DeadlineAlert existing = getById(id);
        existing.setStatus("VALIDATED");
        existing.setUpdatedAt(Instant.now());
        repository.save(existing);
    }

    @Transactional
    public void simulate(UUID id) {
        DeadlineAlert existing = getById(id);
        existing.setStatus("SIMULATED");
        existing.setUpdatedAt(Instant.now());
        repository.save(existing);
    }
}
