package com.microsaas.billingai.service;

import com.microsaas.billingai.model.DunningFlow;
import com.microsaas.billingai.repository.DunningFlowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DunningFlowService {
    private final DunningFlowRepository repository;

    public List<DunningFlow> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public DunningFlow findById(UUID tenantId, UUID id) {
        return repository.findById(id)
                .filter(flow -> flow.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Dunning Flow not found"));
    }

    public DunningFlow create(UUID tenantId, DunningFlow flow) {
        flow.setId(UUID.randomUUID());
        flow.setTenantId(tenantId);
        flow.setCreatedAt(OffsetDateTime.now());
        flow.setUpdatedAt(OffsetDateTime.now());
        return repository.save(flow);
    }

    public DunningFlow update(UUID tenantId, UUID id, DunningFlow flowUpdate) {
        DunningFlow existing = findById(tenantId, id);
        existing.setName(flowUpdate.getName());
        existing.setStatus(flowUpdate.getStatus());
        existing.setMetadataJson(flowUpdate.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    public boolean validate(UUID tenantId, UUID id) {
        findById(tenantId, id);
        return true;
    }
}
