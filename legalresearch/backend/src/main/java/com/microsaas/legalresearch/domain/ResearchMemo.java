package com.microsaas.legalresearch.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "research_memos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResearchMemo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String query;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "practice_area")
    private PracticeArea practiceArea;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemoStatus status;

    @Column(nullable = false, name = "tenant_id")
    private UUID tenantId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id")
    private ResearchThread thread;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}
