package com.microsaas.deploysignal;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface DeploymentRepository extends JpaRepository<Deployment, UUID> {
    List<Deployment> findByTenantId(UUID tenantId);
}
