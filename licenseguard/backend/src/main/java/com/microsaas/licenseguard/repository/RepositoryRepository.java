package com.microsaas.licenseguard.repository;

import com.microsaas.licenseguard.domain.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Repository
public interface RepositoryRepository extends JpaRepository<Repository, UUID> {
    List<Repository> findByTenantId(UUID tenantId);
}
