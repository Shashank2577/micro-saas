package com.microsaas.financenarrator.repository;

import com.microsaas.financenarrator.model.NarrativeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NarrativeRequestRepository extends JpaRepository<NarrativeRequest, UUID> {
    List<NarrativeRequest> findByTenantId(UUID tenantId);
    Optional<NarrativeRequest> findByIdAndTenantId(UUID id, UUID tenantId);
}
