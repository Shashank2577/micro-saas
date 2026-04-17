package com.microsaas.peopleanalytics.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "headcount_plan")
@Data
public class HeadcountPlan {
    @Id
    private UUID id;
    private UUID tenantId;
    private String name;
    private String fiscalYear;
    private String scenarioType;
    private int totalHeadcount;
    private BigDecimal totalCost;
    private LocalDateTime createdAt;
}
