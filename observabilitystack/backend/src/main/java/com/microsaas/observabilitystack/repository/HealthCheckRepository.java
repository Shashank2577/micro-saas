package com.microsaas.observabilitystack.repository;

import com.microsaas.observabilitystack.entity.HealthCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HealthCheckRepository extends JpaRepository<HealthCheck, UUID> {
    List<HealthCheck> findByTenantId(String tenantId);
    Optional<HealthCheck> findByIdAndTenantId(UUID id, String tenantId);
}
