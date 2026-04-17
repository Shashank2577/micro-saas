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
@Table(name = "investor_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestorSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private DataRoom room;

    @Column(nullable = false, name = "investor_id")
    private UUID investorId;

    @ManyToMany
    @JoinTable(
        name = "session_documents",
        joinColumns = @JoinColumn(name = "session_id"),
        inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private List<Document> documentsViewed;

    @Column(name = "time_spent")
    private Long timeSpent;

    @ElementCollection
    @CollectionTable(name = "session_questions", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "question")
    private List<String> questionsAsked;

    @Column(nullable = false, name = "tenant_id")
    private UUID tenantId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}
