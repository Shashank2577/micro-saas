package com.microsaas.backupvault.repository;

import com.microsaas.backupvault.entity.DisasterRecoveryDrill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface DisasterRecoveryDrillRepository extends JpaRepository<DisasterRecoveryDrill, UUID> {
    List<DisasterRecoveryDrill> findByTenantId(String tenantId);
}
