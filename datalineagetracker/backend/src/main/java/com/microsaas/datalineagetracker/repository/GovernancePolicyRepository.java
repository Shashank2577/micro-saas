package com.microsaas.datalineagetracker.repository;

import com.microsaas.datalineagetracker.entity.GovernancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GovernancePolicyRepository extends JpaRepository<GovernancePolicy, UUID> {
    List<GovernancePolicy> findAllByTenantId(UUID tenantId);
    Optional<GovernancePolicy> findByIdAndTenantId(UUID id, UUID tenantId);
}
