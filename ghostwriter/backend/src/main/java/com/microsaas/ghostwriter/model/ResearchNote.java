package com.microsaas.ghostwriter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "research_notes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResearchNote {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @Column(name = "source_url", columnDefinition = "TEXT")
    private String sourceUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String excerpt;

    @Column(columnDefinition = "TEXT")
    private String citation;

    @Column(name = "added_at", updatable = false)
    private Instant addedAt;

    @PrePersist
    public void prePersist() {
        if (addedAt == null) {
            addedAt = Instant.now();
        }
    }
}
