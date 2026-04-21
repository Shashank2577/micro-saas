package com.microsaas.experimentengine.domain.repository;

import com.microsaas.experimentengine.domain.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<Goal, UUID> {
    List<Goal> findByTenantId(UUID tenantId);
    Optional<Goal> findByIdAndTenantId(UUID id, UUID tenantId);
}
