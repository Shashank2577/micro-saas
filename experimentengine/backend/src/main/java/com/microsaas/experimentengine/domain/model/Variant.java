package com.microsaas.experimentengine.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "variant")
@Data
public class Variant {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "experiment_id", nullable = false)
    private UUID experimentId;

    @Column(nullable = false)
    private String name;

    @Column(name = "traffic_percent", nullable = false)
    private Double trafficPercent;

    @Column(name = "feature_flag_key")
    private String featureFlagKey;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
