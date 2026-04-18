package com.microsaas.datagovernanceos.repository;

import com.microsaas.datagovernanceos.entity.ComplianceAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ComplianceAuditRepository extends JpaRepository<ComplianceAudit, UUID> {
    List<ComplianceAudit> findByTenantId(UUID tenantId);
    List<ComplianceAudit> findByTenantIdAndAssetId(UUID tenantId, UUID assetId);
}
