package com.microsaas.contextlayer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contextlayer.domain.ContextVersion;
import com.microsaas.contextlayer.domain.CustomerContext;
import com.microsaas.contextlayer.dto.ContextUpdateDTO;
import com.microsaas.contextlayer.repository.ContextVersionRepository;
import com.microsaas.contextlayer.repository.CustomerContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContextUpdateService {
    private final CustomerContextRepository customerContextRepository;
    private final ContextVersionRepository contextVersionRepository;
    private final RealtimeSyncService realtimeSyncService;

    @Transactional
    public CustomerContext updateContext(String customerId, ContextUpdateDTO updateDTO, String appId) {
        UUID tenantId = TenantContext.require();
        CustomerContext context = customerContextRepository.findByCustomerIdAndTenantId(customerId, tenantId)
                .orElseGet(() -> {
                    CustomerContext newContext = new CustomerContext();
                    newContext.setCustomerId(customerId);
                    newContext.setTenantId(tenantId);
                    return newContext;
                });

        if (updateDTO.getProfile() != null) context.setProfile(updateDTO.getProfile());
        if (updateDTO.getPreferences() != null) context.setPreferences(updateDTO.getPreferences());
        if (updateDTO.getAttributes() != null) context.setAttributes(updateDTO.getAttributes());

        context.setLastUpdatedAt(Instant.now());
        context.setUpdatedByApp(appId);

        CustomerContext savedContext = customerContextRepository.save(context);

        createVersionSnapshot(savedContext, "Context updated by " + appId);
        realtimeSyncService.publishContextUpdate(savedContext);

        return savedContext;
    }

    private void createVersionSnapshot(CustomerContext context, String description) {
        ContextVersion version = new ContextVersion();
        version.setCustomerId(context.getCustomerId());
        version.setTenantId(context.getTenantId());
        String snapshot = String.format("{\"profile\":%s,\"preferences\":%s,\"attributes\":%s}",
            context.getProfile(), context.getPreferences(), context.getAttributes());
        version.setContextSnapshot(snapshot);
        version.setCreatedAt(Instant.now());
        version.setCreatedByApp(context.getUpdatedByApp());
        version.setChangeDescription(description);
        contextVersionRepository.save(version);
    }

    public CustomerContext updateAttribute(String customerId, String attribute, String value, String appId) {
        CustomerContext context = customerContextRepository.findByCustomerIdAndTenantId(customerId, TenantContext.require())
            .orElseThrow(() -> new IllegalArgumentException("Context not found"));

        // Very basic JSON manipulation
        String currentAttributes = context.getAttributes();
        if (currentAttributes == null || currentAttributes.equals("{}")) {
            context.setAttributes("{\"" + attribute + "\":\"" + value + "\"}");
        } else {
            // Simplified logic to avoid complex JSON parsing dependencies
            context.setAttributes(currentAttributes.substring(0, currentAttributes.length() - 1) + ",\"" + attribute + "\":\"" + value + "\"}");
        }

        context.setLastUpdatedAt(java.time.Instant.now());
        context.setUpdatedByApp(appId);

        CustomerContext savedContext = customerContextRepository.save(context);
        createVersionSnapshot(savedContext, "Attribute " + attribute + " updated by " + appId);
        realtimeSyncService.publishContextUpdate(savedContext);

        return savedContext;
    }

    public void exportContext(String customerId) {
        // Implementation for exporting context
    }

    public List<ContextVersion> getContextVersions(String customerId) {
        return contextVersionRepository.findByCustomerIdAndTenantIdOrderByCreatedAtDesc(customerId, TenantContext.require());
    }

    @Transactional
    public void rollbackContext(String customerId, UUID versionId) {
        ContextVersion version = contextVersionRepository.findById(versionId)
            .orElseThrow(() -> new IllegalArgumentException("Version not found"));
        // Simplified rollback for example purposes
        CustomerContext context = customerContextRepository.findByCustomerIdAndTenantId(customerId, TenantContext.require())
            .orElseThrow(() -> new IllegalArgumentException("Context not found"));

        context.setProfile(version.getContextSnapshot()); // A real implementation would parse the JSON
        context.setLastUpdatedAt(Instant.now());
        context.setUpdatedByApp("contextlayer-rollback");
        customerContextRepository.save(context);
        realtimeSyncService.publishContextUpdate(context);
    }

    @Transactional
    public void deleteContext(String customerId) {
        customerContextRepository.deleteByCustomerIdAndTenantId(customerId, TenantContext.require());
    }
}
