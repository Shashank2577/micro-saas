package com.microsaas.featureflagai.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "rollout_metrics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolloutMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "flag_id", nullable = false)
    private UUID flagId;

    @Column(name = "error_rate", nullable = false)
    private double errorRate;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

    @PrePersist
    public void prePersist() {
        if (timestamp == null) {
            timestamp = OffsetDateTime.now();
        }
    }
}
