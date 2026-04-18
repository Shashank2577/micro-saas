package com.microsaas.videonarrator.repository;

import com.microsaas.videonarrator.model.VideoProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VideoProjectRepository extends JpaRepository<VideoProject, UUID> {
    List<VideoProject> findAllByTenantId(String tenantId);
    Optional<VideoProject> findByIdAndTenantId(UUID id, String tenantId);
}
