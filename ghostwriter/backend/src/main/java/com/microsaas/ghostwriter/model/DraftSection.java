package com.microsaas.ghostwriter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "draft_sections")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DraftSection {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String heading;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "word_count")
    private Integer wordCount;

    @Column(name = "section_order", nullable = false)
    private Integer sectionOrder;

    @PrePersist
    public void prePersist() {
        if (wordCount == null) {
            wordCount = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (content != null) {
            wordCount = content.split("\\s+").length;
        }
    }
}
