package com.microsaas.auditready.repository;

import com.microsaas.auditready.model.AuditReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuditReportRepository extends JpaRepository<AuditReport, UUID> {
    List<AuditReport> findByTenantId(UUID tenantId);
    List<AuditReport> findByTenantIdAndFrameworkId(UUID tenantId, UUID frameworkId);
    Optional<AuditReport> findByIdAndTenantId(UUID id, UUID tenantId);
}
