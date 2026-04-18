package com.marketplacehub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "apps")
@Data
@NoArgsConstructor
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String category;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> tags;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> screenshots;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<PricingTier> pricingTiers;

    @Column(name = "developer_id")
    private String developerId;

    private String version;

    private String status = "PUBLIC";

    @Column(name = "trending_score")
    private Integer trendingScore = 0;

    @Column(name = "total_installations")
    private Integer totalInstallations = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
