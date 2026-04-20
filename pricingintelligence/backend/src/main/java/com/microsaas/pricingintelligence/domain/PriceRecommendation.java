package com.microsaas.pricingintelligence.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "price_recommendations")
@Data
public class PriceRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "segment_id", nullable = false)
    private UUID segmentId;

    @Column(name = "current_price", nullable = false)
    private BigDecimal currentPrice;

    @Column(name = "recommended_price", nullable = false)
    private BigDecimal recommendedPrice;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Column(name = "estimated_revenue_lift")
    private Double estimatedRevenueLift;

    @Column(columnDefinition = "text")
    private String rationale;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
