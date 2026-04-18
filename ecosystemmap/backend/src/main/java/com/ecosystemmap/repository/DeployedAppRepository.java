package com.ecosystemmap.repository;

import com.ecosystemmap.domain.DeployedApp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeployedAppRepository extends JpaRepository<DeployedApp, UUID> {
    List<DeployedApp> findByTenantId(UUID tenantId);
    Optional<DeployedApp> findByTenantIdAndAppId(UUID tenantId, String appId);
}
