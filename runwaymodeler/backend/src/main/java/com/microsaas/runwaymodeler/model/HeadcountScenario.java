package com.microsaas.runwaymodeler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "headcount_scenario")
@Data
public class HeadcountScenario {
    @Id
    private UUID id;
    private UUID modelId;
    private String name;
    private int additionalHeads;
    private BigDecimal monthlyCostPerHead;
    private LocalDate startDate;
    private int impactOnRunwayDays;
    private UUID tenantId;
}
