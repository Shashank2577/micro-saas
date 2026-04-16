package com.microsaas.contractsense.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "contract_clauses", schema = "contractsense")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractClause {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID contractId;

    @Column(nullable = false)
    private String clauseType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskLevel riskLevel;

    @Column(nullable = false)
    private boolean deviationFromStandard;

    @Column(nullable = false)
    private UUID tenantId;
}
