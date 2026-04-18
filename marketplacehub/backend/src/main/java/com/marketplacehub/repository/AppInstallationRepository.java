package com.marketplacehub.repository;

import com.marketplacehub.model.AppInstallation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppInstallationRepository extends JpaRepository<AppInstallation, UUID> {
    List<AppInstallation> findByTenantId(UUID tenantId);
    List<AppInstallation> findByTenantIdAndWorkspaceId(UUID tenantId, String workspaceId);
}
