package com.microsaas.auditready.repository;

import com.microsaas.auditready.model.AuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AuditTrailRepository extends JpaRepository<AuditTrail, UUID> {
    List<AuditTrail> findByTenantId(UUID tenantId);
    List<AuditTrail> findByTenantIdAndEntityTypeAndEntityId(UUID tenantId, String entityType, UUID entityId);
}
