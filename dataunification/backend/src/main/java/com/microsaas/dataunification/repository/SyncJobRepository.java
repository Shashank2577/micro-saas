package com.microsaas.dataunification.repository;

import com.microsaas.dataunification.model.SyncJob;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface SyncJobRepository extends JpaRepository<SyncJob, UUID> {
    List<SyncJob> findByTenantId(UUID tenantId);
}
