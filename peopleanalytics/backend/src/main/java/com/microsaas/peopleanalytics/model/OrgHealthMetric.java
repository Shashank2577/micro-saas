package com.microsaas.peopleanalytics.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "org_health_metric")
@Data
public class OrgHealthMetric {
    @Id
    private UUID id;
    private UUID tenantId;
    private String period;
    private BigDecimal spanOfControlAvg;
    private BigDecimal managementRatio;
    private BigDecimal turnoverRate;
    private BigDecimal timeToFillAvg;
    private LocalDateTime computedAt;
}
