package com.microsaas.equityintelligence.model;

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
@Table(name = "equity_grant")
public class EquityGrant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(name = "stakeholder_id", nullable = false)
    private UUID stakeholderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GrantType grantType;

    @Column(nullable = false)
    private Long shares;

    @Column(nullable = false)
    private LocalDate grantDate;

    @Column(nullable = false)
    private Integer cliffMonths;

    @Column(nullable = false)
    private Integer vestMonths;

    private BigDecimal strikePrice;
}
