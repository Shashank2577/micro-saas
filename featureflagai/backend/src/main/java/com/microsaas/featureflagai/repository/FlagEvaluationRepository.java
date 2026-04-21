package com.microsaas.featureflagai.repository;

import com.microsaas.featureflagai.domain.FlagEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlagEvaluationRepository extends JpaRepository<FlagEvaluation, UUID> {
    List<FlagEvaluation> findByTenantIdAndFlagId(UUID tenantId, UUID flagId);
}
