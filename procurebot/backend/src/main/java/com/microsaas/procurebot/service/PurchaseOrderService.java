package com.microsaas.procurebot.service;

import com.microsaas.procurebot.dto.PurchaseOrderRequest;
import com.microsaas.procurebot.model.PurchaseOrder;
import com.microsaas.procurebot.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository repository;

    @Transactional(readOnly = true)
    public List<PurchaseOrder> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public PurchaseOrder findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found"));
    }

    @Transactional
    public PurchaseOrder create(UUID tenantId, PurchaseOrderRequest request) {
        PurchaseOrder entity = PurchaseOrder.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .metadataJson(request.getMetadataJson())
                .build();
        return repository.save(entity);
    }

    @Transactional
    public PurchaseOrder update(UUID id, UUID tenantId, PurchaseOrderRequest request) {
        PurchaseOrder entity = findById(id, tenantId);
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getMetadataJson() != null) entity.setMetadataJson(request.getMetadataJson());
        return repository.save(entity);
    }

    public void validate(UUID id, UUID tenantId) {
        PurchaseOrder entity = findById(id, tenantId);
        // Add business validation logic here
    }
}
