package com.microsaas.dataunification.repository;

import com.microsaas.dataunification.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByTenantId(UUID tenantId);
}
