package com.microsaas.prospectiq.repository;

import com.microsaas.prospectiq.model.Prospect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProspectRepository extends JpaRepository<Prospect, UUID> {
    List<Prospect> findByTenantId(UUID tenantId);
    Optional<Prospect> findByIdAndTenantId(UUID id, UUID tenantId);
    List<Prospect> findByTenantIdAndIndustry(UUID tenantId, String industry);
    List<Prospect> findByTenantIdAndRegion(UUID tenantId, String region);
}
