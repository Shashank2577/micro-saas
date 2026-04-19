package com.microsaas.cashflowanalyzer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.cashflowanalyzer.model.TrendSignal;
import com.microsaas.cashflowanalyzer.repository.TrendSignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InsightsService {
    private final TrendSignalRepository repository;

    @Transactional
    public TrendSignal create(TrendSignal signal) {
        signal.setTenantId(TenantContext.require());
        return repository.save(signal);
    }

    @Transactional
    public TrendSignal update(UUID id, TrendSignal signal) {
        TrendSignal existing = getById(id);
        existing.setName(signal.getName());
        existing.setStatus(signal.getStatus());
        existing.setMetadataJson(signal.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<TrendSignal> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public TrendSignal getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("TrendSignal not found"));
    }

    @Transactional
    public void delete(UUID id) {
        TrendSignal existing = getById(id);
        repository.delete(existing);
    }

    @Transactional(readOnly = true)
    public boolean validate(UUID id) {
        getById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public String simulate(UUID id) {
        TrendSignal signal = getById(id);
        return "Simulated insights for " + signal.getName();
    }
}
