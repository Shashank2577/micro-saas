package com.microsaas.datastoryteller.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "narrative_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NarrativeReport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataset_id", nullable = false)
    private Dataset dataset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private NarrativeTemplate template;

    @Column(nullable = false)
    private String title;

    @Column(name = "content_markdown")
    private String contentMarkdown;

    @Column(name = "time_range_start")
    private OffsetDateTime timeRangeStart;

    @Column(name = "time_range_end")
    private OffsetDateTime timeRangeEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    @Column(name = "generated_at")
    private OffsetDateTime generatedAt;

    @Column(name = "token_usage")
    private Integer tokenUsage;

    @Column
    private String model;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
