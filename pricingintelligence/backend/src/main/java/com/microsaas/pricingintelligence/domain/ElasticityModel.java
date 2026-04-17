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
@Table(name = "elasticity_model")
public class ElasticityModel {
    @Id
    private UUID id;
    private UUID tenantId;
    private String segment;
    private BigDecimal basePrice;
    private BigDecimal elasticityCoefficient;
    private BigDecimal confidenceScore;
    private LocalDateTime lastUpdatedAt;
    private LocalDateTime createdAt;
}
