package com.microsaas.brandvoice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_findings")
@Getter
@Setter
public class AuditFinding {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_result_id", nullable = false)
    private AuditResult auditResult;

    @Column(name = "finding_type", nullable = false)
    private String findingType; // TONE_MISMATCH, FORBIDDEN_WORD, STRUCTURAL_ISSUE

    @Column(name = "original_text", nullable = false, columnDefinition = "text")
    private String originalText;

    @Column(name = "suggested_rewrite", columnDefinition = "text")
    private String suggestedRewrite;

    @Column(name = "explanation", columnDefinition = "text")
    private String explanation;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
