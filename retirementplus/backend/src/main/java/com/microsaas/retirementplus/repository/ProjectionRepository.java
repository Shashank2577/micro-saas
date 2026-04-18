package com.microsaas.retirementplus.repository;

import com.microsaas.retirementplus.domain.Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectionRepository extends JpaRepository<Projection, UUID> {
    Optional<Projection> findByUserIdAndTenantId(UUID userId, UUID tenantId);
}
