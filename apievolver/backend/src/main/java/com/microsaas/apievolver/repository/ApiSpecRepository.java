package com.microsaas.apievolver.repository;

import com.microsaas.apievolver.model.ApiSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApiSpecRepository extends JpaRepository<ApiSpec, UUID> {
    List<ApiSpec> findByTenantId(UUID tenantId);
    Optional<ApiSpec> findByIdAndTenantId(UUID id, UUID tenantId);
}
