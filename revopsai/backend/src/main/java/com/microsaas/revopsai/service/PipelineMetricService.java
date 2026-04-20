package com.microsaas.revopsai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.revopsai.model.PipelineMetric;
import com.microsaas.revopsai.repository.PipelineMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PipelineMetricService {
    private final PipelineMetricRepository repository;

    public List<PipelineMetric> getAll() {
        UUID tenantId = TenantContext.require();
        return repository.findByTenantId(tenantId);
    }

    public PipelineMetric create(PipelineMetric entity) {
        entity.setTenantId(TenantContext.require());
        return repository.save(entity);
    }
}
