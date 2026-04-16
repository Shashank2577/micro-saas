package com.microsaas.auditready.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_readiness_scores")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditReadinessScore {
    @Id
    private UUID id;
    private UUID frameworkId;
    private int score;
    private int controlsMet;
    private int controlsMissing;
    private Instant calculatedAt;
    private UUID tenantId;
}
