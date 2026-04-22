package com.microsaas.dependencyradar.repository;

import com.microsaas.dependencyradar.model.ScanJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScanJobRepository extends JpaRepository<ScanJob, UUID> {
    List<ScanJob> findByTenantId(String tenantId);
    Optional<ScanJob> findByIdAndTenantId(UUID id, String tenantId);
}
