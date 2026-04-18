package com.microsaas.apimanager.repository;

import com.microsaas.apimanager.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {
    List<ApiKey> findByProjectIdAndTenantId(UUID projectId, String tenantId);
    Optional<ApiKey> findByIdAndTenantId(UUID id, String tenantId);
}
