package com.microsaas.workspacemanager.repository;

import com.microsaas.workspacemanager.domain.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, UUID> {
    List<WorkspaceMember> findByTenantId(UUID tenantId);
    List<WorkspaceMember> findByTenantIdAndRole(UUID tenantId, String role);
}
