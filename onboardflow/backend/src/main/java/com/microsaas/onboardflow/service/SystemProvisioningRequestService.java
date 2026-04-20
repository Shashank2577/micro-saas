package com.microsaas.onboardflow.service;

import com.microsaas.onboardflow.dto.SystemProvisioningRequestRequest;
import com.microsaas.onboardflow.model.SystemProvisioningRequest;
import com.microsaas.onboardflow.repository.SystemProvisioningRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SystemProvisioningRequestService {

    private final SystemProvisioningRequestRepository repository;

    @Transactional(readOnly = true)
    public List<SystemProvisioningRequest> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public SystemProvisioningRequest findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("SystemProvisioningRequest not found"));
    }

    @Transactional
    public SystemProvisioningRequest create(UUID tenantId, SystemProvisioningRequestRequest request) {
        SystemProvisioningRequest entity = SystemProvisioningRequest.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .metadataJson(request.getMetadataJson())
                .build();
        return repository.save(entity);
    }

    @Transactional
    public SystemProvisioningRequest update(UUID id, UUID tenantId, SystemProvisioningRequestRequest request) {
        SystemProvisioningRequest entity = findById(id, tenantId);
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getMetadataJson() != null) entity.setMetadataJson(request.getMetadataJson());
        return repository.save(entity);
    }

    public void validate(UUID id, UUID tenantId) {
        SystemProvisioningRequest entity = findById(id, tenantId);
        // Add business validation logic here
    }
}
