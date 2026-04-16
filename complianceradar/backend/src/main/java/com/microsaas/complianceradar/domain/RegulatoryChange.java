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
@Table(name = "regulatory_changes", schema = "complianceradar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegulatoryChange {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String jurisdiction;
    private String regulationName;
    private String summary;
    private LocalDate effectiveDate;

    @Enumerated(EnumType.STRING)
    private ImpactLevel impactLevel;

    private UUID tenantId;

    @CreationTimestamp
    private ZonedDateTime createdAt;
}
