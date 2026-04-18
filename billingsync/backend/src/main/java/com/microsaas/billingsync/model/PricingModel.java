package com.microsaas.billingsync.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pricing_models")
public class PricingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    @JsonIgnore
    private SubscriptionPlan plan;

    @Column(name = "metric_name", nullable = false)
    private String metricName;

    @Column(name = "model_type", nullable = false)
    private String modelType; // FLAT_RATE, PER_UNIT, TIERED, VOLUME

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "included_units", nullable = false)
    private Integer includedUnits;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
