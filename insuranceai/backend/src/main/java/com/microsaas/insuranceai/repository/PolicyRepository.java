package com.microsaas.insuranceai.repository;

import com.microsaas.insuranceai.domain.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, UUID> {
    List<Policy> findByTenantId(UUID tenantId);
    Optional<Policy> findByIdAndTenantId(UUID id, UUID tenantId);
}
