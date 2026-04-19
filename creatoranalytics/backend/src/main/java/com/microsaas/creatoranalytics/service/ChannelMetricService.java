package com.microsaas.creatoranalytics.service;

import com.microsaas.creatoranalytics.model.ChannelMetric;
import com.microsaas.creatoranalytics.repository.ChannelMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelMetricService {
    private final ChannelMetricRepository repository;

    @Transactional(readOnly = true)
    public List<ChannelMetric> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public ChannelMetric findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("ChannelMetric not found"));
    }

    @Transactional
    public ChannelMetric create(ChannelMetric entity, UUID tenantId) {
        entity.setTenantId(tenantId);
        return repository.save(entity);
    }

    @Transactional
    public ChannelMetric update(UUID id, ChannelMetric update, UUID tenantId) {
        ChannelMetric existing = findById(id, tenantId);
        if (update.getName() != null) existing.setName(update.getName());
        if (update.getStatus() != null) existing.setStatus(update.getStatus());
        if (update.getMetadata() != null) existing.setMetadata(update.getMetadata());
        return repository.save(existing);
    }
}
