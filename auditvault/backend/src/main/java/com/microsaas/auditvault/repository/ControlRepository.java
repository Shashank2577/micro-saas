package com.microsaas.auditvault.repository;

import com.microsaas.auditvault.model.Control;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ControlRepository extends JpaRepository<Control, UUID> {
    List<Control> findByTenantIdAndFrameworkId(UUID tenantId, UUID frameworkId);
    List<Control> findByTenantId(UUID tenantId);
}
