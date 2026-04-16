package com.microsaas.copyoptimizer.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "copy_variants")
public class CopyVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "experiment_id", nullable = false)
    private UUID experimentId;

    @Column(name = "variant_name", nullable = false)
    private String variantName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String headline;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String cta;

    @Column(name = "predicted_conversion")
    private BigDecimal predictedConversion;

    @Column(name = "actual_conversion")
    private BigDecimal actualConversion;

    @Column(nullable = false)
    private Integer impressions = 0;

    @Column(nullable = false)
    private Integer conversions = 0;

    @Column(nullable = false)
    private String status;
}
