package com.microsaas.equityintelligence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "funding_round")
public class FundingRound {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String roundName;

    @Column(nullable = false)
    private BigDecimal preMoneyValuation;

    @Column(nullable = false)
    private BigDecimal amountRaised;

    @Column(nullable = false)
    private BigDecimal sharePrice;

    private LocalDateTime closedAt;
}
