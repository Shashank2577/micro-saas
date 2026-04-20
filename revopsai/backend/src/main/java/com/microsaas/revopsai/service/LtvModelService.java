package com.microsaas.revopsai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.revopsai.model.LtvModel;
import com.microsaas.revopsai.repository.LtvModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LtvModelService {
    private final LtvModelRepository repository;

    public List<LtvModel> getAll() {
        UUID tenantId = TenantContext.require();
        return repository.findByTenantId(tenantId);
    }

    public LtvModel create(LtvModel entity) {
        entity.setTenantId(TenantContext.require());
        return repository.save(entity);
    }
}
