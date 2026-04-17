package com.microsaas.apigatekeeper.repository;

import com.microsaas.apigatekeeper.entity.CachePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CachePolicyRepository extends JpaRepository<CachePolicy, UUID> {
    List<CachePolicy> findByTenantId(String tenantId);
    Optional<CachePolicy> findByIdAndTenantId(UUID id, String tenantId);
}
