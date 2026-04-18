package com.microsaas.integrationmesh.repository;

import com.microsaas.integrationmesh.model.Connector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConnectorRepository extends JpaRepository<Connector, UUID> {
    List<Connector> findByTenantId(UUID tenantId);
    Optional<Connector> findByIdAndTenantId(UUID id, UUID tenantId);
}
