package com.microsaas.integrationbridge.service;

import com.microsaas.integrationbridge.model.AuditLog;
import com.microsaas.integrationbridge.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void log(UUID tenantId, UUID integrationId, String action, String status, int recordsProcessed, String errorMessage) {
        AuditLog log = new AuditLog();
        log.setTenantId(tenantId);
        log.setIntegrationId(integrationId);
        log.setAction(action);
        log.setStatus(status);
        log.setRecordsProcessed(recordsProcessed);
        log.setErrorMessage(errorMessage);
        auditLogRepository.save(log);
    }
    
    public List<AuditLog> getLogs(UUID integrationId, UUID tenantId) {
        return auditLogRepository.findByIntegrationIdAndTenantIdOrderByCreatedAtDesc(integrationId, tenantId);
    }
}
