package com.microsaas.workspacemanager.repository;

import com.microsaas.workspacemanager.domain.SsoDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SsoDomainRepository extends JpaRepository<SsoDomain, UUID> {
    List<SsoDomain> findByTenantId(UUID tenantId);
}
