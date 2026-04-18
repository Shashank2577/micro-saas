package com.microsaas.meetingbrain.repository;

import com.microsaas.meetingbrain.model.ProjectLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectLinkRepository extends JpaRepository<ProjectLink, UUID> {
    List<ProjectLink> findByTenantIdAndProjectId(UUID tenantId, UUID projectId);
}
