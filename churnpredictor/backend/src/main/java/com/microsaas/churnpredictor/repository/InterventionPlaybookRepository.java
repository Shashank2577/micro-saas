package com.microsaas.churnpredictor.repository;

import com.microsaas.churnpredictor.entity.InterventionPlaybook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InterventionPlaybookRepository extends JpaRepository<InterventionPlaybook, UUID> {
    List<InterventionPlaybook> findByTenantId(UUID tenantId);
    Optional<InterventionPlaybook> findByIdAndTenantId(UUID id, UUID tenantId);
    List<InterventionPlaybook> findByTenantIdAndIsActiveTrue(UUID tenantId);
}
