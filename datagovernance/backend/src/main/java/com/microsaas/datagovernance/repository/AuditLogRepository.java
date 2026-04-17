package com.microsaas.datagovernance.repository;

import com.microsaas.datagovernance.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByTenantIdAndTimestampBetween(UUID tenantId, LocalDateTime start, LocalDateTime end);
    List<AuditLog> findByTenantId(UUID tenantId);
}
