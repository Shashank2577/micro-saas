package com.microsaas.contractsense.repository;

import com.microsaas.contractsense.domain.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, UUID> {
    Optional<RiskAssessment> findByContractIdAndTenantId(UUID contractId, UUID tenantId);
}
