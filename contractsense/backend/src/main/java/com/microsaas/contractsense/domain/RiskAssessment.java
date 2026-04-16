package com.microsaas.contractsense.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "risk_assessments", schema = "contractsense")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID contractId;

    @Column(nullable = false)
    private int overallRiskScore;

    @Column(nullable = false)
    private int flagCount;

    @Column(nullable = false)
    private int missingProvisionsCount;

    @Column(columnDefinition = "TEXT")
    private String recommendations;

    @Column(nullable = false)
    private UUID tenantId;
}
