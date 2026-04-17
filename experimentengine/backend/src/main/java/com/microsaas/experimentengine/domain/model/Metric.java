package com.microsaas.experimentengine.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "metric")
@Data
public class Metric {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "experiment_id", nullable = false)
    private UUID experimentId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetricType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetricRole role;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
