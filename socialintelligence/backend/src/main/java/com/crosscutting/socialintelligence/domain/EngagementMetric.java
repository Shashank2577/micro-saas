



package com.crosscutting.socialintelligence.domain;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;





import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "engagement_metrics")
public class EngagementMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private PlatformAccount account;

    @Column(name = "metric_date", nullable = false)
    private LocalDate metricDate;

    @Column(name = "followers_count")
    private Long followersCount = 0L;

    @Column(name = "following_count")
    private Long followingCount = 0L;

    @Column(name = "likes_count")
    private Long likesCount = 0L;

    @Column(name = "comments_count")
    private Long commentsCount = 0L;

    @Column(name = "shares_count")
    private Long sharesCount = 0L;

    @Column(name = "views_count")
    private Long viewsCount = 0L;

    @Column(name = "engagement_rate")
    private BigDecimal engagementRate;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}
