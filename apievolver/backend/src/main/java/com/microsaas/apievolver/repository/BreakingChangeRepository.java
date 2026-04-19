package com.microsaas.apievolver.repository;

import com.microsaas.apievolver.model.BreakingChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BreakingChangeRepository extends JpaRepository<BreakingChange, UUID> {
    List<BreakingChange> findByTenantId(UUID tenantId);
    Optional<BreakingChange> findByIdAndTenantId(UUID id, UUID tenantId);
}
