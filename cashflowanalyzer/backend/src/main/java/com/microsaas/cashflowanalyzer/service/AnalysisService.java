package com.microsaas.cashflowanalyzer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.cashflowanalyzer.model.CashMovement;
import com.microsaas.cashflowanalyzer.repository.CashMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final CashMovementRepository repository;

    @Transactional
    public CashMovement create(CashMovement movement) {
        movement.setTenantId(TenantContext.require());
        return repository.save(movement);
    }

    @Transactional
    public CashMovement update(UUID id, CashMovement movement) {
        CashMovement existing = getById(id);
        existing.setName(movement.getName());
        existing.setStatus(movement.getStatus());
        existing.setMetadataJson(movement.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<CashMovement> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public CashMovement getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("CashMovement not found"));
    }

    @Transactional
    public void delete(UUID id) {
        CashMovement existing = getById(id);
        repository.delete(existing);
    }

    @Transactional(readOnly = true)
    public boolean validate(UUID id) {
        getById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public String simulate(UUID id) {
        CashMovement movement = getById(id);
        return "Simulated analysis for " + movement.getName();
    }
}
