package com.microsaas.legalresearch.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "research_threads")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResearchThread {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "practice_area")
    private PracticeArea practiceArea;

    @Column(nullable = false, name = "tenant_id")
    private UUID tenantId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}
