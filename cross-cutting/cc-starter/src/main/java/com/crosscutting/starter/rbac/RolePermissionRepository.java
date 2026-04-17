package com.crosscutting.starter.rbac;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {

    List<RolePermission> findByRoleIdIn(List<UUID> roleIds);

    void deleteByRoleId(UUID roleId);
}
