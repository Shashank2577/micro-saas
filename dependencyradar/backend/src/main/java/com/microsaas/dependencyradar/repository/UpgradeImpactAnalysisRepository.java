package com.microsaas.dependencyradar.repository;

import com.microsaas.dependencyradar.model.UpgradeImpactAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UpgradeImpactAnalysisRepository extends JpaRepository<UpgradeImpactAnalysis, UUID> {
    List<UpgradeImpactAnalysis> findByTenantId(String tenantId);
    Optional<UpgradeImpactAnalysis> findByIdAndTenantId(UUID id, String tenantId);
}
