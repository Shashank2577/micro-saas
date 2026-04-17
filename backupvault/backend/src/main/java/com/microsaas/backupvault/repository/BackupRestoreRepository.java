package com.microsaas.backupvault.repository;

import com.microsaas.backupvault.entity.BackupRestore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BackupRestoreRepository extends JpaRepository<BackupRestore, UUID> {
    List<BackupRestore> findByTenantId(String tenantId);
}
