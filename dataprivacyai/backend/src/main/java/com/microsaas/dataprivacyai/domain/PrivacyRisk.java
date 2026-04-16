package com.microsaas.dataprivacyai.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "privacy_risks", schema = "dataprivacyai")
@Getter
@Setter
public class PrivacyRisk {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "data_flow_id")
    private UUID dataFlowId;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_type")
    private RiskType riskType;

    @Enumerated(EnumType.STRING)
    private RiskSeverity severity;

    private String description;

    @Enumerated(EnumType.STRING)
    private RiskStatus status;

    @Column(name = "tenant_id")
    private UUID tenantId;
}
