package com.microsaas.auditready.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditReport {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID frameworkId;
    private String reportName;
    private Double readinessScore;
    private ZonedDateTime generatedAt;
    private String summary;
}
