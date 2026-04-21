package com.microsaas.securitypulse;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TriagedReportRepository extends JpaRepository<TriagedReport, UUID> {
    List<TriagedReport> findByTenantId(UUID tenantId);
    TriagedReport findByScanJobIdAndTenantId(UUID scanJobId, UUID tenantId);
}
