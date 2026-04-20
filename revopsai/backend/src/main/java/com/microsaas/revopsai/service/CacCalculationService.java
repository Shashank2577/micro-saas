package com.microsaas.revopsai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.revopsai.model.CacCalculation;
import com.microsaas.revopsai.repository.CacCalculationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CacCalculationService {
    private final CacCalculationRepository repository;

    public List<CacCalculation> getAll() {
        UUID tenantId = TenantContext.require();
        return repository.findByTenantId(tenantId);
    }

    public CacCalculation create(CacCalculation entity) {
        entity.setTenantId(TenantContext.require());
        return repository.save(entity);
    }
}
