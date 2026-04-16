package com.microsaas.peopleanalytics.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "headcount_line")
@Data
public class HeadcountLine {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID planId;
    private String department;
    private String role;
    private int currentCount;
    private int plannedCount;
    private BigDecimal avgCost;
    private String startQuarter;
}
