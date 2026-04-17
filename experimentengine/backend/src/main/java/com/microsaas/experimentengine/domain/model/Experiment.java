package com.microsaas.experimentengine.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "experiment")
@Data
public class Experiment {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String hypothesis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExperimentStatus status = ExperimentStatus.DRAFT;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    @Column(name = "min_sample_size")
    private Integer minSampleSize;

    @Column(name = "significance_threshold")
    private Double significanceThreshold;

    @Column(name = "peek_protection", nullable = false)
    private boolean peekProtection = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
