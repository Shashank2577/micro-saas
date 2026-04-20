package com.microsaas.revopsai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.revopsai.model.ForecastAccuracy;
import com.microsaas.revopsai.repository.ForecastAccuracyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForecastAccuracyService {
    private final ForecastAccuracyRepository repository;

    public List<ForecastAccuracy> getAll() {
        UUID tenantId = TenantContext.require();
        return repository.findByTenantId(tenantId);
    }

    public ForecastAccuracy create(ForecastAccuracy entity) {
        entity.setTenantId(TenantContext.require());
        return repository.save(entity);
    }
}
