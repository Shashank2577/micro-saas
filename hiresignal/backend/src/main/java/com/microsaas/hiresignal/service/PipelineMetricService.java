package com.microsaas.hiresignal.service;

import com.microsaas.hiresignal.model.PipelineMetric;
import com.microsaas.hiresignal.repository.PipelineMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class PipelineMetricService {

    private final PipelineMetricRepository repository;

    public List<PipelineMetric> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<PipelineMetric> findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public PipelineMetric save(PipelineMetric entity) {
        return repository.save(entity);
    }

    public PipelineMetric update(UUID id, UUID tenantId, PipelineMetric updateData) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(existing -> {
                    existing.setName(updateData.getName());
                    existing.setStatus(updateData.getStatus());
                    existing.setMetadataJson(updateData.getMetadataJson());
                    return repository.save(existing);
                }).orElseThrow(() -> new RuntimeException("PipelineMetric not found"));
    }

    public Map<String, Object> validate(UUID id, UUID tenantId) {
        Optional<PipelineMetric> entity = repository.findByIdAndTenantId(id, tenantId);
        Map<String, Object> response = new HashMap<>();
        if (entity.isPresent()) {
            response.put("valid", true);
            response.put("message", "PipelineMetric is valid.");
        } else {
            response.put("valid", false);
            response.put("message", "PipelineMetric not found.");
        }
        return response;
    }
}
