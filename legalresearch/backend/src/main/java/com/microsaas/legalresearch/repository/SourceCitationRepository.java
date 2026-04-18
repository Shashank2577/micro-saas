package com.microsaas.legalresearch.repository;

import com.microsaas.legalresearch.domain.SourceCitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SourceCitationRepository extends JpaRepository<SourceCitation, UUID> {
    List<SourceCitation> findByTenantId(UUID tenantId);
    Optional<SourceCitation> findByIdAndTenantId(UUID id, UUID tenantId);
}
