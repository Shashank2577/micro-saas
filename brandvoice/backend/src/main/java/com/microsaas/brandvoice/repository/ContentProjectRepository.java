package com.microsaas.brandvoice.repository;

import com.microsaas.brandvoice.entity.ContentProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContentProjectRepository extends JpaRepository<ContentProject, UUID> {
    List<ContentProject> findByTenantId(UUID tenantId);
}
