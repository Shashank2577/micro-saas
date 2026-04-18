package com.microsaas.engagementpulse.repository;

import com.microsaas.engagementpulse.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, UUID> {
    List<Alert> findByTenantIdAndResolvedFalse(UUID tenantId);
}
