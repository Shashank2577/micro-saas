package com.microsaas.contractportfolio.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "extracted_terms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtractedTerm {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "contract_id", nullable = false)
    private UUID contractId;

    @Enumerated(EnumType.STRING)
    @Column(name = "term_type", nullable = false)
    private TermType termType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String value;

    @Column(name = "confidence_score", nullable = false)
    private double confidenceScore;

    @Column(name = "source_text", columnDefinition = "TEXT")
    private String sourceText;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
