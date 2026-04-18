package com.microsaas.nonprofitos.repository;

import com.microsaas.nonprofitos.domain.Grant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GrantRepository extends JpaRepository<Grant, UUID> {
    List<Grant> findByTenantId(UUID tenantId);
    Optional<Grant> findByIdAndTenantId(UUID id, UUID tenantId);
}
