package com.microsaas.apigatekeeper.repository;

import com.microsaas.apigatekeeper.entity.RateLimitPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RateLimitPolicyRepository extends JpaRepository<RateLimitPolicy, UUID> {
    List<RateLimitPolicy> findByTenantId(String tenantId);
    Optional<RateLimitPolicy> findByIdAndTenantId(UUID id, String tenantId);
}
