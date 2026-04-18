package com.microsaas.agencyos.repository;

import com.microsaas.agencyos.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByTenantId(String tenantId);
    List<Project> findByTenantIdAndClientId(String tenantId, UUID clientId);
    Optional<Project> findByIdAndTenantId(UUID id, String tenantId);
}
