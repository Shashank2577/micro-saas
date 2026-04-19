package com.microsaas.cashflowanalyzer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.cashflowanalyzer.model.AnomalyFlag;
import com.microsaas.cashflowanalyzer.repository.AnomalyFlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnomaliesService {
    private final AnomalyFlagRepository repository;

    @Transactional
    public AnomalyFlag create(AnomalyFlag flag) {
        flag.setTenantId(TenantContext.require());
        return repository.save(flag);
    }

    @Transactional
    public AnomalyFlag update(UUID id, AnomalyFlag flag) {
        AnomalyFlag existing = getById(id);
        existing.setName(flag.getName());
        existing.setStatus(flag.getStatus());
        existing.setMetadataJson(flag.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<AnomalyFlag> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public AnomalyFlag getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("AnomalyFlag not found"));
    }

    @Transactional
    public void delete(UUID id) {
        AnomalyFlag existing = getById(id);
        repository.delete(existing);
    }

    @Transactional(readOnly = true)
    public boolean validate(UUID id) {
        getById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public String simulate(UUID id) {
        AnomalyFlag flag = getById(id);
        return "Simulated anomalies for " + flag.getName();
    }
}
