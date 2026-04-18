package com.microsaas.nonprofitos.repository;

import com.microsaas.nonprofitos.domain.Impact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImpactRepository extends JpaRepository<Impact, UUID> {
    List<Impact> findByTenantId(UUID tenantId);
    Optional<Impact> findByIdAndTenantId(UUID id, UUID tenantId);
}
