package com.microsaas.backupvault.repository;

import com.microsaas.backupvault.entity.BackupPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BackupPolicyRepository extends JpaRepository<BackupPolicy, UUID> {
    List<BackupPolicy> findByTenantId(String tenantId);
}
