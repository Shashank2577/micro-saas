package com.microsaas.integrationmesh.repository;

import com.microsaas.integrationmesh.model.SyncHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SyncHistoryRepository extends JpaRepository<SyncHistory, UUID> {
    List<SyncHistory> findByIntegrationIdAndTenantIdOrderByStartedAtDesc(UUID integrationId, UUID tenantId);
}
