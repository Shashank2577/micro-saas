package com.microsaas.pricingintelligence.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "churn_analyses")
@Data
public class ChurnAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "segment_id", nullable = false)
    private UUID segmentId;

    @Column(name = "price_tier")
    private String priceTier;

    @Column(name = "churn_rate")
    private Double churnRate;

    @Column(name = "price_sensitivity")
    private Double priceSensitivity;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
