package com.microsaas.compbenchmark.repositories;

import com.microsaas.compbenchmark.model.GapFinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GapFindingRepository extends JpaRepository<GapFinding, UUID> {
    List<GapFinding> findByTenantId(UUID tenantId);
    Optional<GapFinding> findByIdAndTenantId(UUID id, UUID tenantId);
}
