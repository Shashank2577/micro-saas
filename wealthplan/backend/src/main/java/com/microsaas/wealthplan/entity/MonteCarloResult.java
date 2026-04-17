package com.microsaas.wealthplan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "monte_carlo_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonteCarloResult {
    @Id
    private UUID id = UUID.randomUUID();
    private String tenantId;
    private UUID scenarioId;
    private BigDecimal successProbability;
    private BigDecimal medianEndingBalance;
    private BigDecimal worstCaseBalance;
    private BigDecimal bestCaseBalance;
    private LocalDateTime simulationDate = LocalDateTime.now();
}
