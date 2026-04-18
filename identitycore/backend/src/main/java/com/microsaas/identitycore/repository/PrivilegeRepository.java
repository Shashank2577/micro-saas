package com.microsaas.identitycore.repository;

import com.microsaas.identitycore.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, UUID> {
    List<Privilege> findByTenantId(UUID tenantId);
    List<Privilege> findByTenantIdAndUserId(UUID tenantId, UUID userId);
}
