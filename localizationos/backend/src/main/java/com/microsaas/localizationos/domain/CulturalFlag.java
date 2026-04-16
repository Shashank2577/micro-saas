package com.microsaas.localizationos.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "cultural_flags")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulturalFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID jobId;

    @Column(nullable = false)
    private String phrase;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueType issueType;

    @Column(nullable = false)
    private String market;

    @Column(nullable = false)
    private String suggestion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;
}
