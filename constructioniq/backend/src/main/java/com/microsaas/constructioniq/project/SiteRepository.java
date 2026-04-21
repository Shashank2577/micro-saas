package com.microsaas.constructioniq.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface SiteRepository extends JpaRepository<Site, UUID> {
    List<Site> findByProjectIdAndTenantId(UUID projectId, UUID tenantId);
    Optional<Site> findByIdAndTenantId(UUID id, UUID tenantId);
}
