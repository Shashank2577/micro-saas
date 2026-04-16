package com.microsaas.complianceradar.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "compliance_gaps", schema = "complianceradar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplianceGap {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID regulatoryChangeId;
    private UUID policyId;
    private String gapDescription;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    private GapStatus status;

    private String owner;

    private UUID tenantId;

    @CreationTimestamp
    private ZonedDateTime createdAt;
}
