package com.microsaas.processminer.repository;

import com.microsaas.processminer.domain.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, UUID> {
    List<Policy> findByTenantId(UUID tenantId);
    List<Policy> findByTenantIdAndProcessModelId(UUID tenantId, UUID processModelId);
}
