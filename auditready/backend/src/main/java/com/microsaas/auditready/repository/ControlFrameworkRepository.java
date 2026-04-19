package com.microsaas.auditready.repository;

import com.microsaas.auditready.model.ControlFramework;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ControlFrameworkRepository extends JpaRepository<ControlFramework, UUID> {
    List<ControlFramework> findByTenantId(UUID tenantId);
    Optional<ControlFramework> findByIdAndTenantId(UUID id, UUID tenantId);
}
