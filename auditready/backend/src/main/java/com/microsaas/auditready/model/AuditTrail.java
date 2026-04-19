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
@Table(name = "audit_trail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditTrail {
    @Id
    private UUID id;
    private UUID tenantId;
    private String entityType;
    private UUID entityId;
    private String action;
    private String performedBy;
    private ZonedDateTime timestamp;
}
