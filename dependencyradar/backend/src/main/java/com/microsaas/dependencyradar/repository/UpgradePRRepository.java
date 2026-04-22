package com.microsaas.dependencyradar.repository;

import com.microsaas.dependencyradar.model.UpgradePR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UpgradePRRepository extends JpaRepository<UpgradePR, UUID> {
    List<UpgradePR> findByTenantId(String tenantId);
    Optional<UpgradePR> findByIdAndTenantId(UUID id, String tenantId);
}
