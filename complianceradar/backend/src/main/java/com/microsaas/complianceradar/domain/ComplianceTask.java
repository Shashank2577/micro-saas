package com.microsaas.complianceradar.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "compliance_tasks", schema = "complianceradar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplianceTask {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID gapId;
    private String description;
    private LocalDate dueDate;
    private String assignedTo;
    private ZonedDateTime completedAt;

    private UUID tenantId;

    @CreationTimestamp
    private ZonedDateTime createdAt;
}
