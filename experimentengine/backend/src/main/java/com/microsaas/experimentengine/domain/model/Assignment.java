package com.microsaas.experimentengine.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "assignment")
@Data
public class Assignment {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "experiment_id", nullable = false)
    private UUID experimentId;

    @Column(name = "unit_id", nullable = false)
    private String unitId;

    @Column(name = "variant_id", nullable = false)
    private UUID variantId;

    @CreationTimestamp
    @Column(name = "assigned_at", nullable = false, updatable = false)
    private Instant assignedAt;
}
