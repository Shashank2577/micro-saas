package com.microsaas.brandvoice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_results")
@Getter
@Setter
public class AuditResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_audit_id", nullable = false)
    private ContentAudit contentAudit;

    @Column(name = "consistency_score", nullable = false)
    private Integer consistencyScore;

    @Column(name = "sentiment_alignment")
    private String sentimentAlignment;

    @Column(name = "summary_notes", columnDefinition = "text")
    private String summaryNotes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
