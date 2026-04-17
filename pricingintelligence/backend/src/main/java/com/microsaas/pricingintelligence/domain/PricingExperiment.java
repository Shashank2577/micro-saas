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
@Table(name = "pricing_experiment")
public class PricingExperiment {
    @Id
    private UUID id;
    private UUID tenantId;
    private String name;
    private String segment;
    private BigDecimal controlPrice;
    private BigDecimal variantPrice;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
}
