package com.microsaas.cashflowanalyzer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.cashflowanalyzer.model.CashflowPeriod;
import com.microsaas.cashflowanalyzer.repository.CashflowPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngestionService {
    private final CashflowPeriodRepository repository;

    @Transactional
    public CashflowPeriod create(CashflowPeriod period) {
        period.setTenantId(TenantContext.require());
        return repository.save(period);
    }

    @Transactional
    public CashflowPeriod update(UUID id, CashflowPeriod period) {
        CashflowPeriod existing = getById(id);
        existing.setName(period.getName());
        existing.setStatus(period.getStatus());
        existing.setMetadataJson(period.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<CashflowPeriod> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public CashflowPeriod getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("CashflowPeriod not found"));
    }

    @Transactional
    public void delete(UUID id) {
        CashflowPeriod existing = getById(id);
        repository.delete(existing);
    }

    @Transactional(readOnly = true)
    public boolean validate(UUID id) {
        getById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public String simulate(UUID id) {
        CashflowPeriod period = getById(id);
        return "Simulated ingestion for " + period.getName();
    }
}
