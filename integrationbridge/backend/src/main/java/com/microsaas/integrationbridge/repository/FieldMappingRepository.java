package com.microsaas.integrationbridge.repository;

import com.microsaas.integrationbridge.model.FieldMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FieldMappingRepository extends JpaRepository<FieldMapping, UUID> {
    List<FieldMapping> findBySyncJobIdAndTenantId(UUID syncJobId, UUID tenantId);
}
