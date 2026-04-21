package com.crosscutting.socialintelligence.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "content_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentPost {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID tenantId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private PlatformAccount account;
    private String externalPostId;
    private String contentType;
    private String caption;
    private LocalDateTime postedAt;
    private Long likes;
    private Long comments;
    private Long shares;
    private Long views;
    private BigDecimal engagementRate;
}
