package com.microsaas.performancenarrative.repository;

import com.microsaas.performancenarrative.entity.GoalEvidence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface GoalEvidenceRepository extends JpaRepository<GoalEvidence, UUID> {
    List<GoalEvidence> findByTenantId(UUID tenantId);
    Optional<GoalEvidence> findByIdAndTenantId(UUID id, UUID tenantId);
}
