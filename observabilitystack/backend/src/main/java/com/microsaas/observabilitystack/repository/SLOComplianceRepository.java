package com.microsaas.observabilitystack.repository;

import com.microsaas.observabilitystack.entity.SLOCompliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SLOComplianceRepository extends JpaRepository<SLOCompliance, UUID> {
    List<SLOCompliance> findByTenantId(String tenantId);
    Optional<SLOCompliance> findByIdAndTenantId(UUID id, String tenantId);
}
