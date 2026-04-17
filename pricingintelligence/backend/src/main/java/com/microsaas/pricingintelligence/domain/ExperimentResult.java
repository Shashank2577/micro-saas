package com.microsaas.pricingintelligence.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "experiment_result")
public class ExperimentResult {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID experimentId;
    private String variantType;
    private BigDecimal conversionRate;
    private BigDecimal revenueImpact;
    private Boolean isSignificant;
    private LocalDateTime createdAt;
}
