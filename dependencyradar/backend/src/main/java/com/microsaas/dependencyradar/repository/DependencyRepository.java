package com.microsaas.dependencyradar.repository;

import com.microsaas.dependencyradar.model.Dependency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DependencyRepository extends JpaRepository<Dependency, UUID> {
    List<Dependency> findByTenantId(String tenantId);
    Optional<Dependency> findByIdAndTenantId(UUID id, String tenantId);
}
