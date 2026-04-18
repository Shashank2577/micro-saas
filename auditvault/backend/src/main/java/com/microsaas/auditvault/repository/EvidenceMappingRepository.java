package com.microsaas.auditvault.repository;

import com.microsaas.auditvault.model.EvidenceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EvidenceMappingRepository extends JpaRepository<EvidenceMapping, UUID> {
    List<EvidenceMapping> findByTenantId(UUID tenantId);
    List<EvidenceMapping> findByTenantIdAndEvidenceId(UUID tenantId, UUID evidenceId);
}
