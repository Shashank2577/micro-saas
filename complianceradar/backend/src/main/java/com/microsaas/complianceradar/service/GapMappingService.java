package com.microsaas.complianceradar.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.complianceradar.domain.ControlGap;
import com.microsaas.complianceradar.repository.ControlGapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GapMappingService {

    private final ControlGapRepository repository;

    @Transactional(readOnly = true)
    public List<ControlGap> list() {
        return repository.findAllByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public ControlGap getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("ControlGap not found"));
    }

    @Transactional
    public ControlGap create(ControlGap gap) {
        gap.setId(UUID.randomUUID());
        gap.setTenantId(TenantContext.require());
        gap.setCreatedAt(Instant.now());
        return repository.save(gap);
    }

    @Transactional
    public ControlGap update(UUID id, ControlGap updateDetails) {
        ControlGap existing = getById(id);
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
        ControlGap existing = getById(id);
        existing.setStatus("VALIDATED");
        existing.setUpdatedAt(Instant.now());
        repository.save(existing);
    }

    @Transactional
    public void simulate(UUID id) {
        ControlGap existing = getById(id);
        existing.setStatus("SIMULATED");
        existing.setUpdatedAt(Instant.now());
        repository.save(existing);
    }
}
