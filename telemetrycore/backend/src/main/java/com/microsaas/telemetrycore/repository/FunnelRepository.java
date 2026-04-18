package com.microsaas.telemetrycore.repository;

import com.microsaas.telemetrycore.model.Funnel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FunnelRepository extends JpaRepository<Funnel, UUID> {
    List<Funnel> findByTenantId(UUID tenantId);
    Optional<Funnel> findByIdAndTenantId(UUID id, UUID tenantId);
}
