package com.microsaas.cashflowanalyzer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.cashflowanalyzer.model.NarrativeInsight;
import com.microsaas.cashflowanalyzer.repository.NarrativeInsightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportingService {
    private final NarrativeInsightRepository repository;

    @Transactional
    public NarrativeInsight create(NarrativeInsight insight) {
        insight.setTenantId(TenantContext.require());
        return repository.save(insight);
    }

    @Transactional
    public NarrativeInsight update(UUID id, NarrativeInsight insight) {
        NarrativeInsight existing = getById(id);
        existing.setName(insight.getName());
        existing.setStatus(insight.getStatus());
        existing.setMetadataJson(insight.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<NarrativeInsight> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public NarrativeInsight getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("NarrativeInsight not found"));
    }

    @Transactional
    public void delete(UUID id) {
        NarrativeInsight existing = getById(id);
        repository.delete(existing);
    }

    @Transactional(readOnly = true)
    public boolean validate(UUID id) {
        getById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public String simulate(UUID id) {
        NarrativeInsight insight = getById(id);
        return "Simulated reporting for " + insight.getName();
    }
}
