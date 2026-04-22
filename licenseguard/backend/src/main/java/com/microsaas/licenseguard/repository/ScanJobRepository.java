package com.microsaas.licenseguard.repository;

import com.microsaas.licenseguard.domain.ScanJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScanJobRepository extends JpaRepository<ScanJob, UUID> {
    List<ScanJob> findByTenantId(UUID tenantId);
    List<ScanJob> findByRepositoryIdAndTenantId(UUID repositoryId, UUID tenantId);
}
