package com.microsaas.careerpath.repository;

import com.microsaas.careerpath.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    List<Role> findAllByTenantId(String tenantId);
    Optional<Role> findByIdAndTenantId(UUID id, String tenantId);
}
