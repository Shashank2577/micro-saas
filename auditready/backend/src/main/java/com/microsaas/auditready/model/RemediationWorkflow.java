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
@Table(name = "remediation_workflow")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RemediationWorkflow {
    @Id
    private UUID id;
    private UUID tenantId;
    private UUID gapId;
    private String title;
    private String description;
    private String assignedTo;
    private String status;
    private ZonedDateTime dueDate;
}
