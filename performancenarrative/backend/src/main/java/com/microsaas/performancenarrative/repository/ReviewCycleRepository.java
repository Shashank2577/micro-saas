package com.microsaas.performancenarrative.repository;

import com.microsaas.performancenarrative.entity.ReviewCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCycleRepository extends JpaRepository<ReviewCycle, UUID> {
    List<ReviewCycle> findByTenantId(UUID tenantId);
    Optional<ReviewCycle> findByIdAndTenantId(UUID id, UUID tenantId);
}
