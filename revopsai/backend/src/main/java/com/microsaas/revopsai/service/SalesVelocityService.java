package com.microsaas.revopsai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.revopsai.model.SalesVelocity;
import com.microsaas.revopsai.repository.SalesVelocityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SalesVelocityService {
    private final SalesVelocityRepository repository;

    public List<SalesVelocity> getAll() {
        UUID tenantId = TenantContext.require();
        return repository.findByTenantId(tenantId);
    }

    public SalesVelocity create(SalesVelocity entity) {
        entity.setTenantId(TenantContext.require());
        return repository.save(entity);
    }
}
