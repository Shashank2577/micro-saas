package com.marketplacehub.repository;

import com.marketplacehub.model.PermissionGrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PermissionGrantRepository extends JpaRepository<PermissionGrant, UUID> {
    List<PermissionGrant> findByTenantIdAndInstallationId(UUID tenantId, UUID installationId);
}
