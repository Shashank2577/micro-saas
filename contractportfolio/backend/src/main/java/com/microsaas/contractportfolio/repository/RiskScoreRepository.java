package com.microsaas.contractportfolio.repository;

import com.microsaas.contractportfolio.domain.RiskScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RiskScoreRepository extends JpaRepository<RiskScore, UUID> {
    List<RiskScore> findAllByTenantId(UUID tenantId);
    Optional<RiskScore> findByIdAndTenantId(UUID id, UUID tenantId);
}
