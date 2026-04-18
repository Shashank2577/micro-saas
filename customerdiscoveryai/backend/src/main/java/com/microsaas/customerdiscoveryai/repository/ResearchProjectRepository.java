package com.microsaas.customerdiscoveryai.repository;

import com.microsaas.customerdiscoveryai.model.ResearchProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResearchProjectRepository extends JpaRepository<ResearchProject, UUID> {
    List<ResearchProject> findByTenantId(UUID tenantId);
    Optional<ResearchProject> findByIdAndTenantId(UUID id, UUID tenantId);
}
