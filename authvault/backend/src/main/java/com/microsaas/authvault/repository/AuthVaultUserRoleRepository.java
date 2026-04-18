package com.microsaas.authvault.repository;

import com.microsaas.authvault.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AuthVaultUserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {
    List<UserRole> findByUserId(UUID userId);
}
