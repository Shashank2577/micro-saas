package com.microsaas.budgetpilot.repository;

import com.microsaas.budgetpilot.model.Variance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VarianceRepository extends JpaRepository<Variance, UUID> {
    Optional<Variance> findByIdAndTenantId(UUID id, UUID tenantId);
}
