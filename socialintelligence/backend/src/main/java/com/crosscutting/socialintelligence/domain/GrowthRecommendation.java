



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
@Table(name = "growth_recommendations")
public class GrowthRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "platform_name", nullable = false)
    private String platformName;

    @Column(name = "recommendation_type", nullable = false)
    private String recommendationType;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "projected_impact")
    private String projectedImpact;

    @Column(name = "status")
    private String status = "NEW";

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private ZonedDateTime updatedAt;
}
