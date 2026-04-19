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
@Table(name = "compliance_gap")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplianceGap {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID controlId;
    private String description;
    private String severity;
    private String status;
    private ZonedDateTime detectedAt;
}
