package com.microsaas.ndaflow.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Entity
@Table(name = "nda_clauses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NdaClause {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nda_id", nullable = false)
    private UUID ndaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "clause_type", nullable = false)
    private ClauseType clauseType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_negotiable", nullable = false)
    @Builder.Default
    private boolean isNegotiable = true;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
}
