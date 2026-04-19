package com.microsaas.complianceradar.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.complianceradar.domain.ImpactAssessment;
import com.microsaas.complianceradar.repository.ImpactAssessmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImpactAssessmentService {

    private final ImpactAssessmentRepository repository;

    @Transactional(readOnly = true)
    public List<ImpactAssessment> list() {
        return repository.findAllByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public ImpactAssessment getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("ImpactAssessment not found"));
    }

    @Transactional
    public ImpactAssessment create(ImpactAssessment assessment) {
        assessment.setId(UUID.randomUUID());
        assessment.setTenantId(TenantContext.require());
        assessment.setCreatedAt(Instant.now());
        return repository.save(assessment);
    }

    @Transactional
    public ImpactAssessment update(UUID id, ImpactAssessment updateDetails) {
        ImpactAssessment existing = getById(id);
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
        ImpactAssessment existing = getById(id);
        existing.setStatus("VALIDATED");
        existing.setUpdatedAt(Instant.now());
        repository.save(existing);
    }

    @Transactional
    public void simulate(UUID id) {
        ImpactAssessment existing = getById(id);
        existing.setStatus("SIMULATED");
        existing.setUpdatedAt(Instant.now());
        repository.save(existing);
    }
}
