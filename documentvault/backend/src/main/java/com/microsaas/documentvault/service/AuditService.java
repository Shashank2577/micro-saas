package com.microsaas.documentvault.service;

import com.microsaas.documentvault.model.AuditLog;
import com.microsaas.documentvault.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(UUID tenantId, UUID documentId, UUID userId, String action, String ip, String userAgent) {
        AuditLog log = new AuditLog();
        log.setTenantId(tenantId);
        log.setDocumentId(documentId);
        log.setUserId(userId);
        log.setAction(action);
        log.setIpAddress(ip);
        log.setUserAgent(userAgent);
        auditLogRepository.save(log);
    }
    
    public List<AuditLog> getLogs(UUID documentId, UUID tenantId) {
        return auditLogRepository.findByDocumentIdAndTenantIdOrderByCreatedAtDesc(documentId, tenantId);
    }
}
