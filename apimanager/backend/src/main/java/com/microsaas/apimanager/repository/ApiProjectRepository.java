package com.microsaas.apimanager.repository;

import com.microsaas.apimanager.model.ApiProject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApiProjectRepository extends JpaRepository<ApiProject, UUID> {
    List<ApiProject> findByTenantId(String tenantId);
    Optional<ApiProject> findByIdAndTenantId(UUID id, String tenantId);
}
