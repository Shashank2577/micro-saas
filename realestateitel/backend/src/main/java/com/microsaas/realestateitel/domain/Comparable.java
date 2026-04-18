package com.microsaas.realestateitel.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comparables")
@Data
@NoArgsConstructor
public class Comparable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_property_id", nullable = false)
    private Property subjectProperty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comp_property_id", nullable = false)
    private Property compProperty;

    @Column(name = "similarity_score")
    private BigDecimal similarityScore;

    @Column(name = "price_adjusted")
    private BigDecimal priceAdjusted;

    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
