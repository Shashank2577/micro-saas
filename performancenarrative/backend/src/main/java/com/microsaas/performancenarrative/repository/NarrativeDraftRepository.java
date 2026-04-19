package com.microsaas.performancenarrative.repository;

import com.microsaas.performancenarrative.entity.NarrativeDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface NarrativeDraftRepository extends JpaRepository<NarrativeDraft, UUID> {
    List<NarrativeDraft> findByTenantId(UUID tenantId);
    Optional<NarrativeDraft> findByIdAndTenantId(UUID id, UUID tenantId);
}
