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
@Table(name = "compliance_policies", schema = "complianceradar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompliancePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String content;
    private String owner;
    private ZonedDateTime lastReviewedAt;

    private UUID tenantId;

    @CreationTimestamp
    private ZonedDateTime createdAt;
}
