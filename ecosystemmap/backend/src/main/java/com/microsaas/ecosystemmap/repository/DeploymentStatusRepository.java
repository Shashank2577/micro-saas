package com.microsaas.ecosystemmap.repository;

import com.microsaas.ecosystemmap.entity.DeploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeploymentStatusRepository extends JpaRepository<DeploymentStatus, UUID> {
    List<DeploymentStatus> findByTenantIdAndNodeId(String tenantId, UUID nodeId);
    Optional<DeploymentStatus> findByIdAndTenantId(UUID id, String tenantId);
}
