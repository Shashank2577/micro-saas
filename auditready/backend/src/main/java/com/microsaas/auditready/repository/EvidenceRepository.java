package com.microsaas.auditready.repository;

import com.microsaas.auditready.model.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EvidenceRepository extends JpaRepository<Evidence, UUID> {
    List<Evidence> findByTenantId(UUID tenantId);
    List<Evidence> findByTenantIdAndControlId(UUID tenantId, UUID controlId);
    Optional<Evidence> findByIdAndTenantId(UUID id, UUID tenantId);
}
