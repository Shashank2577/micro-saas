package com.microsaas.brandvoice.repository;

import com.microsaas.brandvoice.entity.AuditFinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditFindingRepository extends JpaRepository<AuditFinding, UUID> {
    List<AuditFinding> findByAuditResultIdAndTenantId(UUID auditResultId, UUID tenantId);
}
