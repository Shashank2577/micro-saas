package com.microsaas.complianceradar.service;

import com.microsaas.complianceradar.domain.ImpactLevel;
import com.microsaas.complianceradar.domain.RegulatoryChange;
import com.microsaas.complianceradar.repository.RegulatoryChangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegulatoryChangeService {
    private final RegulatoryChangeRepository repository;

    @Transactional
    public RegulatoryChange addChange(String jurisdiction, String name, String summary, LocalDate effectiveDate, ImpactLevel impact, UUID tenantId) {
        RegulatoryChange change = RegulatoryChange.builder()
                .jurisdiction(jurisdiction)
                .regulationName(name)
                .summary(summary)
                .effectiveDate(effectiveDate)
                .impactLevel(impact)
                .tenantId(tenantId)
                .build();
        return repository.save(change);
    }

    public List<RegulatoryChange> listChanges(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public RegulatoryChange getChange(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId).orElseThrow(() -> new RuntimeException("Regulatory Change not found"));
    }
}
