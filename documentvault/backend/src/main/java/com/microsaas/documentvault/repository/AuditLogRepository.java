package com.microsaas.documentvault.repository;

import com.microsaas.documentvault.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByDocumentIdAndTenantIdOrderByCreatedAtDesc(UUID documentId, UUID tenantId);
}
