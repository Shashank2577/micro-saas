package com.microsaas.legalresearch.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "citations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Citation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id", nullable = false)
    private ResearchMemo memo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "citation_type")
    private CitationType type;

    @Column(nullable = false)
    private String reference;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false, name = "is_verified")
    @Builder.Default
    private boolean isVerified = false;

    @Column(nullable = false, name = "tenant_id")
    private UUID tenantId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}
