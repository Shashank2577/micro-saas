package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.RetentionPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface RetentionPredictionRepository extends JpaRepository<RetentionPrediction, UUID> {
    List<RetentionPrediction> findAllByTenantId(UUID tenantId);
    List<RetentionPrediction> findAllByTenantIdAndRiskLevel(UUID tenantId, String riskLevel);
}
