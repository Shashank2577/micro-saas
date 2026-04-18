package com.microsaas.datagovernanceos.repository;

import com.microsaas.datagovernanceos.entity.GovernancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GovernancePolicyRepository extends JpaRepository<GovernancePolicy, UUID> {
    List<GovernancePolicy> findByTenantId(UUID tenantId);
    List<GovernancePolicy> findByTenantIdAndStatus(UUID tenantId, String status);
    Optional<GovernancePolicy> findByIdAndTenantId(UUID id, UUID tenantId);
}
