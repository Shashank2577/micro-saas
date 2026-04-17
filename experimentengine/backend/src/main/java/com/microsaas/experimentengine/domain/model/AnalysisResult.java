package com.microsaas.experimentengine.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "analysis_result")
@Data
public class AnalysisResult {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "experiment_id", nullable = false)
    private UUID experimentId;

    @Column(name = "metric_id", nullable = false)
    private UUID metricId;

    @Column(name = "variant_id", nullable = false)
    private UUID variantId;

    @Column(name = "n_exposures", nullable = false)
    private Integer nExposures = 0;

    @Column(name = "n_conversions", nullable = false)
    private Integer nConversions = 0;

    private Double mean;

    @Column(name = "std_err")
    private Double stdErr;

    @Column(name = "p_value")
    private Double pValue;

    @Column(name = "bayesian_prob_better")
    private Double bayesianProbBetter;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
