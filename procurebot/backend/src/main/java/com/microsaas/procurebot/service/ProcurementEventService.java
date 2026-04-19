package com.microsaas.procurebot.service;

import com.microsaas.procurebot.dto.ProcurementEventRequest;
import com.microsaas.procurebot.model.ProcurementEvent;
import com.microsaas.procurebot.repository.ProcurementEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcurementEventService {

    private final ProcurementEventRepository repository;

    @Transactional(readOnly = true)
    public List<ProcurementEvent> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public ProcurementEvent findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("ProcurementEvent not found"));
    }

    @Transactional
    public ProcurementEvent create(UUID tenantId, ProcurementEventRequest request) {
        ProcurementEvent entity = ProcurementEvent.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .metadataJson(request.getMetadataJson())
                .build();
        return repository.save(entity);
    }

    @Transactional
    public ProcurementEvent update(UUID id, UUID tenantId, ProcurementEventRequest request) {
        ProcurementEvent entity = findById(id, tenantId);
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getMetadataJson() != null) entity.setMetadataJson(request.getMetadataJson());
        return repository.save(entity);
    }

    public void validate(UUID id, UUID tenantId) {
        ProcurementEvent entity = findById(id, tenantId);
        // Add business validation logic here
    }
}
