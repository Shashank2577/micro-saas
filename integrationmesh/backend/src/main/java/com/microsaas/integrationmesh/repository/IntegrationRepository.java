package com.microsaas.integrationmesh.repository;

import com.microsaas.integrationmesh.model.Integration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IntegrationRepository extends JpaRepository<Integration, UUID> {
    List<Integration> findByTenantId(UUID tenantId);
    Optional<Integration> findByIdAndTenantId(UUID id, UUID tenantId);
}
