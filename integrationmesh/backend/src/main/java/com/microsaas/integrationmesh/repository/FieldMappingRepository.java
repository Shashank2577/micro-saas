package com.microsaas.integrationmesh.repository;

import com.microsaas.integrationmesh.model.FieldMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FieldMappingRepository extends JpaRepository<FieldMapping, UUID> {
    List<FieldMapping> findByIntegrationIdAndTenantId(UUID integrationId, UUID tenantId);
}
