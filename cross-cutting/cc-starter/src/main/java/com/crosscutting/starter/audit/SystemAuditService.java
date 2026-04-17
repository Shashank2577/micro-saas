package com.crosscutting.starter.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SystemAuditService {

    private final SystemAuditLogRepository repository;

    public SystemAuditService(SystemAuditLogRepository repository) {
        this.repository = repository;
    }

    public Page<SystemAuditLog> findByTenant(UUID tenantId, Pageable pageable) {
        if (tenantId == null) {
            return repository.findAll(pageable);
        }
        return repository.findByTenantIdIncludingNull(tenantId, pageable);
    }

    public Page<SystemAuditLog> findByUser(UUID userId, Pageable pageable) {
        return repository.findByUserId(userId, pageable);
    }

    public List<SystemAuditLog> findByCorrelation(UUID correlationId) {
        return repository.findByCorrelationId(correlationId);
    }

    public Page<SystemAuditLog> findByEventType(String eventType, Pageable pageable) {
        return repository.findByEventType(eventType, pageable);
    }

    public Page<SystemAuditLog> findByTenantAndEventType(UUID tenantId, String eventType, Pageable pageable) {
        return repository.findByTenantIdAndEventType(tenantId, eventType, pageable);
    }

    public Page<SystemAuditLog> findByResource(String resourceType, String resourceId, Pageable pageable) {
        return repository.findByResourceTypeAndResourceId(resourceType, resourceId, pageable);
    }
}
