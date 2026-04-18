package com.microsaas.apimanager.repository;

import com.microsaas.apimanager.model.ApiEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApiEndpointRepository extends JpaRepository<ApiEndpoint, UUID> {
    List<ApiEndpoint> findByVersionIdAndTenantId(UUID versionId, String tenantId);
    Optional<ApiEndpoint> findByIdAndTenantId(UUID id, String tenantId);
}
