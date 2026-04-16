package com.microsaas.auditready.repository;

import com.microsaas.auditready.domain.Control;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ControlRepository extends JpaRepository<Control, UUID> {
    List<Control> findByFrameworkIdAndTenantId(UUID frameworkId, UUID tenantId);
    Optional<Control> findByIdAndTenantId(UUID id, UUID tenantId);
}
