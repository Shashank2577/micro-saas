package com.microsaas.licenseguard.repository;

import com.microsaas.licenseguard.domain.Dependency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DependencyRepository extends JpaRepository<Dependency, UUID> {
    List<Dependency> findByRepositoryIdAndTenantId(UUID repositoryId, UUID tenantId);
}
