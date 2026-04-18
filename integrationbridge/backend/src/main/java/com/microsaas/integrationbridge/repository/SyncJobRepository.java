package com.microsaas.integrationbridge.repository;

import com.microsaas.integrationbridge.model.SyncJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SyncJobRepository extends JpaRepository<SyncJob, UUID> {
    List<SyncJob> findByIntegrationIdAndTenantId(UUID integrationId, UUID tenantId);
    Optional<SyncJob> findByIdAndTenantId(UUID id, UUID tenantId);
    List<SyncJob> findByStatus(String status);
}
