package com.microsaas.taxoptimizer.domain.repository;

import com.microsaas.taxoptimizer.domain.entity.ComplianceChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComplianceChecklistRepository extends JpaRepository<ComplianceChecklistItem, UUID> {
    List<ComplianceChecklistItem> findByTenantIdAndProfileId(UUID tenantId, UUID profileId);
    Optional<ComplianceChecklistItem> findByTenantIdAndId(UUID tenantId, UUID id);
}
