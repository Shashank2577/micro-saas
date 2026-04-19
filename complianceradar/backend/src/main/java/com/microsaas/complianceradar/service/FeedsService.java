package com.microsaas.complianceradar.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.complianceradar.domain.RegulationUpdate;
import com.microsaas.complianceradar.repository.RegulationUpdateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedsService {

    private final RegulationUpdateRepository repository;

    @Transactional(readOnly = true)
    public List<RegulationUpdate> list() {
        return repository.findAllByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public RegulationUpdate getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("RegulationUpdate not found"));
    }

    @Transactional
    public RegulationUpdate create(RegulationUpdate update) {
        update.setId(UUID.randomUUID());
        update.setTenantId(TenantContext.require());
        update.setCreatedAt(Instant.now());
        return repository.save(update);
    }

    @Transactional
    public RegulationUpdate update(UUID id, RegulationUpdate updateDetails) {
        RegulationUpdate existing = getById(id);
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
        RegulationUpdate existing = getById(id);
        existing.setStatus("VALIDATED");
        existing.setUpdatedAt(Instant.now());
        repository.save(existing);
    }

    @Transactional
    public void simulate(UUID id) {
        RegulationUpdate existing = getById(id);
        existing.setStatus("SIMULATED");
        existing.setUpdatedAt(Instant.now());
        repository.save(existing);
    }
}
