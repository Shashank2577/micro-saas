package com.microsaas.authvault.repository;

import com.microsaas.authvault.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthVaultRoleRepository extends JpaRepository<Role, UUID> {
    List<Role> findByTenantId(UUID tenantId);
    Optional<Role> findByTenantIdAndName(UUID tenantId, String name);
}
