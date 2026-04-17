package com.microsaas.agentops.repository;

import com.microsaas.agentops.model.HumanEscalation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HumanEscalationRepository extends JpaRepository<HumanEscalation, UUID> {
    List<HumanEscalation> findByTenantId(UUID tenantId);
    List<HumanEscalation> findByTenantIdAndStatus(UUID tenantId, String status);
    Optional<HumanEscalation> findByIdAndTenantId(UUID id, UUID tenantId);
}
