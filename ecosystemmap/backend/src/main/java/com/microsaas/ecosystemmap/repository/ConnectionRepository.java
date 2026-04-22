package com.microsaas.ecosystemmap.repository;

import com.microsaas.ecosystemmap.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, UUID> {
    List<Connection> findByTenantIdAndEcosystemId(String tenantId, UUID ecosystemId);
    Optional<Connection> findByIdAndTenantId(UUID id, String tenantId);
}
