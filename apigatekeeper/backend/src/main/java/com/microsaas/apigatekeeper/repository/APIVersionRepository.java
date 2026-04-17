package com.microsaas.apigatekeeper.repository;

import com.microsaas.apigatekeeper.entity.APIVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface APIVersionRepository extends JpaRepository<APIVersion, UUID> {
    List<APIVersion> findByTenantId(String tenantId);
    Optional<APIVersion> findByIdAndTenantId(UUID id, String tenantId);
}
