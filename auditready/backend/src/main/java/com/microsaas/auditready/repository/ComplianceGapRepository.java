package com.microsaas.auditready.repository;

import com.microsaas.auditready.model.ComplianceGap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ComplianceGapRepository extends JpaRepository<ComplianceGap, UUID> {
    List<ComplianceGap> findByTenantId(UUID tenantId);
    List<ComplianceGap> findByTenantIdAndControlId(UUID tenantId, UUID controlId);
    Optional<ComplianceGap> findByIdAndTenantId(UUID id, UUID tenantId);
}
