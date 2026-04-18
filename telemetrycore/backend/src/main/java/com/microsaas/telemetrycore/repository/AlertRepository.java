package com.microsaas.telemetrycore.repository;

import com.microsaas.telemetrycore.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlertRepository extends JpaRepository<Alert, UUID> {
    List<Alert> findByTenantId(UUID tenantId);
    Optional<Alert> findByIdAndTenantId(UUID id, UUID tenantId);
}
