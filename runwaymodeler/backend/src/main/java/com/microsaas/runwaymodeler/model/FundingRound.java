package com.microsaas.runwaymodeler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "funding_round")
@Data
public class FundingRound {
    @Id
    private UUID id;
    private UUID modelId;
    private BigDecimal amount;
    private BigDecimal valuationCap;
    private Double dilutionPercent;
    private LocalDate expectedCloseDate;
    private UUID tenantId;
}
