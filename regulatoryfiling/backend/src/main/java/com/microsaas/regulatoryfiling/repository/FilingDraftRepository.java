package com.microsaas.regulatoryfiling.repository;

import com.microsaas.regulatoryfiling.domain.FilingDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilingDraftRepository extends JpaRepository<FilingDraft, UUID> {
    Optional<FilingDraft> findByObligationIdAndTenantId(UUID obligationId, UUID tenantId);
}
