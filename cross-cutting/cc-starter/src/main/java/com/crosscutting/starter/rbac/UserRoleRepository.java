package com.crosscutting.starter.rbac;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    List<UserRole> findByUserIdAndTenantId(UUID userId, UUID tenantId);

    void deleteByUserIdAndRoleIdAndTenantId(UUID userId, UUID roleId, UUID tenantId);
}
