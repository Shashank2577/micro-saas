package com.microsaas.copyoptimizer.repository;

import com.microsaas.copyoptimizer.model.PredictionScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PredictionScoreRepository extends JpaRepository<PredictionScore, UUID> {
    List<PredictionScore> findByTenantId(UUID tenantId);
    Optional<PredictionScore> findByIdAndTenantId(UUID id, UUID tenantId);
}
