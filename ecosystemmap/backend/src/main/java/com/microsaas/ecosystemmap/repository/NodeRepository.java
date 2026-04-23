package com.microsaas.ecosystemmap.repository;

import com.microsaas.ecosystemmap.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NodeRepository extends JpaRepository<Node, UUID> {
    List<Node> findByTenantIdAndEcosystemId(String tenantId, UUID ecosystemId);
    Optional<Node> findByIdAndTenantId(UUID id, String tenantId);
    Optional<Node> findByAppNameAndTenantId(String appName, String tenantId);
}
