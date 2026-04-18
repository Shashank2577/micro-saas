package com.microsaas.integrationbridge.repository;

import com.microsaas.integrationbridge.model.SyncedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface SyncedRecordRepository extends JpaRepository<SyncedRecord, UUID> {
    Optional<SyncedRecord> findByExternalIdAndIntegrationIdAndTenantId(String externalId, UUID integrationId, UUID tenantId);
}
