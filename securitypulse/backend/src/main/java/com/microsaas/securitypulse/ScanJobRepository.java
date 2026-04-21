package com.microsaas.securitypulse;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScanJobRepository extends JpaRepository<ScanJob, UUID> {
    List<ScanJob> findByTenantId(UUID tenantId);
    Optional<ScanJob> findByIdAndTenantId(UUID id, UUID tenantId);
}
