package com.microsaas.datagovernanceos.entity;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "compliance_audits")
@Getter
@Setter
public class ComplianceAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "asset_id", nullable = false)
    private UUID assetId;

    @Column(name = "policy_id", nullable = false)
    private UUID policyId;

    private String status;

    private String findings;

    @CreationTimestamp
    @Column(name = "audited_at", updatable = false)
    private ZonedDateTime auditedAt;
}
