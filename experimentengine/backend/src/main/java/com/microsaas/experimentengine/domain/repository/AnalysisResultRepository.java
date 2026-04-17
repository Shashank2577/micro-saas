package com.microsaas.experimentengine.domain.repository;

import com.microsaas.experimentengine.domain.model.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, UUID> {
    List<AnalysisResult> findByExperimentId(UUID experimentId);
    Optional<AnalysisResult> findByExperimentIdAndMetricIdAndVariantId(UUID experimentId, UUID metricId, UUID variantId);
}
