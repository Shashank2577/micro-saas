package com.microsaas.dataroomai.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ai_answers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private DataRoom room;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    @ManyToMany
    @JoinTable(
        name = "ai_answer_sources",
        joinColumns = @JoinColumn(name = "answer_id"),
        inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private List<Document> sourceDocuments;

    @Column(nullable = false, name = "confidence_score")
    private Double confidenceScore;

    @Column(nullable = false, name = "tenant_id")
    private UUID tenantId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}
