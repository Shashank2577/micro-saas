package com.microsaas.debtnavigator.repository;

import com.microsaas.debtnavigator.entity.OptimizationRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OptimizationRunRepository extends JpaRepository<OptimizationRun, UUID> {
    List<OptimizationRun> findByTenantId(UUID tenantId);
    Optional<OptimizationRun> findByIdAndTenantId(UUID id, UUID tenantId);
}
