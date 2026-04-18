package com.microsaas.apimanager.repository;

import com.microsaas.apimanager.model.ApiVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApiVersionRepository extends JpaRepository<ApiVersion, UUID> {
    List<ApiVersion> findByProjectIdAndTenantId(UUID projectId, String tenantId);
    Optional<ApiVersion> findByIdAndTenantId(UUID id, String tenantId);
}
