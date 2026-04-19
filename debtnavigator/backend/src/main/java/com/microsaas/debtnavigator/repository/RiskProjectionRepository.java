package com.microsaas.debtnavigator.repository;

import com.microsaas.debtnavigator.entity.RiskProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RiskProjectionRepository extends JpaRepository<RiskProjection, UUID> {
    List<RiskProjection> findByTenantId(UUID tenantId);
    Optional<RiskProjection> findByIdAndTenantId(UUID id, UUID tenantId);
}
