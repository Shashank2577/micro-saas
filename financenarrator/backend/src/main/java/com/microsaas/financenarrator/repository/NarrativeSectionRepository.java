package com.microsaas.financenarrator.repository;

import com.microsaas.financenarrator.model.NarrativeSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NarrativeSectionRepository extends JpaRepository<NarrativeSection, UUID> {
    List<NarrativeSection> findByTenantId(UUID tenantId);
    Optional<NarrativeSection> findByIdAndTenantId(UUID id, UUID tenantId);
}
