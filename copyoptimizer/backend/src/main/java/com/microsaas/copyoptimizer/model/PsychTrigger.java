package com.microsaas.copyoptimizer.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "psych_triggers")
public class PsychTrigger {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "variant_id", nullable = false)
    private UUID variantId;

    @Column(name = "trigger_type", nullable = false)
    private String triggerType;

    @Column(name = "usage_text", nullable = false, columnDefinition = "TEXT")
    private String usageText;

    @Column(name = "strength_score", nullable = false)
    private BigDecimal strengthScore;
}
