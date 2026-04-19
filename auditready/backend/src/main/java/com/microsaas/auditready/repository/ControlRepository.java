package com.microsaas.auditready.repository;

import com.microsaas.auditready.model.Control;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ControlRepository extends JpaRepository<Control, UUID> {
    List<Control> findByTenantId(UUID tenantId);
    List<Control> findByTenantIdAndFrameworkId(UUID tenantId, UUID frameworkId);
    Optional<Control> findByIdAndTenantId(UUID id, UUID tenantId);
}
