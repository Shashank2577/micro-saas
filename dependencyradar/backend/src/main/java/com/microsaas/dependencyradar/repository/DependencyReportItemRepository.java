package com.microsaas.dependencyradar.repository;

import com.microsaas.dependencyradar.model.DependencyReportItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DependencyReportItemRepository extends JpaRepository<DependencyReportItem, UUID> {
    List<DependencyReportItem> findByTenantId(String tenantId);
    Optional<DependencyReportItem> findByIdAndTenantId(UUID id, String tenantId);
}
