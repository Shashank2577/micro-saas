



package com.crosscutting.socialintelligence.domain;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "content_analyses")
public class ContentAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private PlatformAccount account;

    @Column(name = "content_id", nullable = false)
    private String contentId;

    @Column(name = "content_url")
    private String contentUrl;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "published_at")
    private ZonedDateTime publishedAt;

    @Column(name = "likes")
    private Long likes = 0L;

    @Column(name = "comments")
    private Long comments = 0L;

    @Column(name = "shares")
    private Long shares = 0L;

    @Column(name = "views")
    private Long views = 0L;

    @Column(name = "ai_pattern_analysis")
    private String aiPatternAnalysis;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}
