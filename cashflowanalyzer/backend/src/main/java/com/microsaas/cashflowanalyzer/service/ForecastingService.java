package com.microsaas.cashflowanalyzer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.cashflowanalyzer.model.ForecastRun;
import com.microsaas.cashflowanalyzer.repository.ForecastRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForecastingService {
    private final ForecastRunRepository repository;

    @Transactional
    public ForecastRun create(ForecastRun run) {
        run.setTenantId(TenantContext.require());
        return repository.save(run);
    }

    @Transactional
    public ForecastRun update(UUID id, ForecastRun run) {
        ForecastRun existing = getById(id);
        existing.setName(run.getName());
        existing.setStatus(run.getStatus());
        existing.setMetadataJson(run.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<ForecastRun> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public ForecastRun getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("ForecastRun not found"));
    }

    @Transactional
    public void delete(UUID id) {
        ForecastRun existing = getById(id);
        repository.delete(existing);
    }

    @Transactional(readOnly = true)
    public boolean validate(UUID id) {
        getById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public String simulate(UUID id) {
        ForecastRun run = getById(id);
        return "Simulated forecasting for " + run.getName();
    }
}
