package com.microsaas.dependencyradar.repository;

import com.microsaas.dependencyradar.model.DependencyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DependencyReportRepository extends JpaRepository<DependencyReport, UUID> {
    List<DependencyReport> findByTenantId(String tenantId);
    Optional<DependencyReport> findByIdAndTenantId(UUID id, String tenantId);
}
