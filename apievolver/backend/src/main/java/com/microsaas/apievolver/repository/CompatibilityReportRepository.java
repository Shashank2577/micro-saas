package com.microsaas.apievolver.repository;

import com.microsaas.apievolver.model.CompatibilityReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompatibilityReportRepository extends JpaRepository<CompatibilityReport, UUID> {
    List<CompatibilityReport> findByTenantId(UUID tenantId);
    Optional<CompatibilityReport> findByIdAndTenantId(UUID id, UUID tenantId);
}
