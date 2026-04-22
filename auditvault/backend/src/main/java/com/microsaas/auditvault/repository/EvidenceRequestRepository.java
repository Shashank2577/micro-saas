package com.microsaas.auditvault.repository;

import com.microsaas.auditvault.model.EvidenceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EvidenceRequestRepository extends JpaRepository<EvidenceRequest, UUID> {
    List<EvidenceRequest> findByTenantId(UUID tenantId);
    List<EvidenceRequest> findByTenantIdAndControlId(UUID tenantId, UUID controlId);
}
