package com.microsaas.prospectiq.repository;

import com.microsaas.prospectiq.model.ProspectBrief;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProspectBriefRepository extends JpaRepository<ProspectBrief, UUID> {
    Optional<ProspectBrief> findFirstByTenantIdAndProspectIdOrderByCreatedAtDesc(UUID tenantId, UUID prospectId);
    List<ProspectBrief> findByTenantIdAndProspectIdOrderByCreatedAtDesc(UUID tenantId, UUID prospectId);
    Optional<ProspectBrief> findByIdAndTenantId(UUID id, UUID tenantId);
}
