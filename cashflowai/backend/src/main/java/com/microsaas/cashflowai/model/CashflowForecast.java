package com.microsaas.cashflowai.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cashflow_forecast")
public class CashflowForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDate periodStart;

    @Column(nullable = false)
    private LocalDate periodEnd;

    @Column(nullable = false)
    private BigDecimal inflowForecast;

    @Column(nullable = false)
    private BigDecimal outflowForecast;

    @Column(nullable = false)
    private BigDecimal netForecast;

    @Column(nullable = false)
    private UUID tenantId;
}
