package com.microsaas.apievolver.repository;

import com.microsaas.apievolver.model.ApiVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApiVersionRepository extends JpaRepository<ApiVersion, UUID> {
    List<ApiVersion> findByTenantId(UUID tenantId);
    Optional<ApiVersion> findByIdAndTenantId(UUID id, UUID tenantId);
}
