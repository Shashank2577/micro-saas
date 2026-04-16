package com.microsaas.auditready.repository;

import com.microsaas.auditready.domain.EvidenceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EvidenceItemRepository extends JpaRepository<EvidenceItem, UUID> {
    List<EvidenceItem> findByControlIdAndTenantId(UUID controlId, UUID tenantId);
    Optional<EvidenceItem> findByIdAndTenantId(UUID id, UUID tenantId);
}
