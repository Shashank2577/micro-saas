package com.microsaas.experimentengine.domain.repository;

import com.microsaas.experimentengine.domain.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
    Optional<Assignment> findByExperimentIdAndUnitId(UUID experimentId, String unitId);
    long countByExperimentId(UUID experimentId);
    long countByVariantId(UUID variantId);
}
