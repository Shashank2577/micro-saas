package com.microsaas.nexushub.app;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EcosystemAppRepository extends JpaRepository<EcosystemApp, UUID> {
    List<EcosystemApp> findByTenantId(UUID tenantId);
    Optional<EcosystemApp> findByTenantIdAndName(UUID tenantId, String name);
    List<EcosystemApp> findByTenantIdAndStatus(UUID tenantId, EcosystemApp.AppStatus status);
}
