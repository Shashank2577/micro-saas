package com.microsaas.hiresignal.repository;

import com.microsaas.hiresignal.model.HiringDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HiringDecisionRepository extends JpaRepository<HiringDecision, UUID> {
    List<HiringDecision> findByTenantId(UUID tenantId);
    Optional<HiringDecision> findByIdAndTenantId(UUID id, UUID tenantId);
}
