package com.microsaas.dependencyradar.repository;

import com.microsaas.dependencyradar.model.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositoryRepository extends JpaRepository<Repository, UUID> {
    List<Repository> findByTenantId(String tenantId);
    Optional<Repository> findByIdAndTenantId(UUID id, String tenantId);
}
