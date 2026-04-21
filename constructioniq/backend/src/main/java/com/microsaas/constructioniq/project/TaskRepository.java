package com.microsaas.constructioniq.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByProjectIdAndTenantId(UUID projectId, UUID tenantId);
    Optional<Task> findByIdAndTenantId(UUID id, UUID tenantId);
}
