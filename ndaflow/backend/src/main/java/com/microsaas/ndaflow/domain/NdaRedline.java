package com.microsaas.ndaflow.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Entity
@Table(name = "nda_redlines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NdaRedline {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nda_id", nullable = false)
    private UUID ndaId;

    @Column(name = "clause_id", nullable = false)
    private UUID clauseId;

    @Column(name = "original_text", nullable = false, columnDefinition = "TEXT")
    private String originalText;

    @Column(name = "proposed_text", nullable = false, columnDefinition = "TEXT")
    private String proposedText;

    @Column(columnDefinition = "TEXT")
    private String rationale;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RedlineStatus status;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
}
