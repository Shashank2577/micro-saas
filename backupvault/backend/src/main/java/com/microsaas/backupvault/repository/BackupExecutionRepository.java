package com.microsaas.backupvault.repository;

import com.microsaas.backupvault.entity.BackupExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BackupExecutionRepository extends JpaRepository<BackupExecution, UUID> {
    List<BackupExecution> findByTenantId(String tenantId);
}
